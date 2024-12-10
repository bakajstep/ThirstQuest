package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.dto.ReviewDTO;
import edu.vutfit.ThirstQuest.dto.WaterBubblerDTO;
import edu.vutfit.ThirstQuest.dto.WaterBubblerIdsDTO;
import edu.vutfit.ThirstQuest.mapper.ReviewMapper;
import edu.vutfit.ThirstQuest.mapper.WaterBubblerMapper;
import edu.vutfit.ThirstQuest.model.*;
import edu.vutfit.ThirstQuest.service.PhotoService;
import edu.vutfit.ThirstQuest.service.ReviewService;
import edu.vutfit.ThirstQuest.service.UserService;
import edu.vutfit.ThirstQuest.service.WaterBubblerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/waterbubblers")
public class WaterBubblerController {

    @Autowired
    private WaterBubblerService waterBubblerService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private WaterBubblerMapper waterBubblerMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private PhotoService photoService;

    @Value("${upload.dir}")
    private String picturePath;

    @GetMapping
    public List<WaterBubblerDTO> getAllWaterBubblers(
        @RequestParam double minLat,
        @RequestParam double maxLat,
        @RequestParam double minLon,
        @RequestParam double maxLon,
        Authentication authentication
    ) {
        AppUser user = null;
        if (authentication != null) {
            String currentUserEmail = authentication.getName();
            user = userService.getByEmail(currentUserEmail);
        }

        List<WaterBubblerDTO> result = new ArrayList<>();
        List<WaterBubbler> bubblers = waterBubblerService.getWaterBubblersWithinCoordinates(minLon, maxLon, minLat, maxLat);

        // If watter bubbler is store in our db that mean it could have review other will have 0
        for (WaterBubbler bubbler : bubblers) {
            int downvote = 0;
            int upvote = 0;

            if (bubbler.getId() != null) {
                downvote = reviewService.countByWaterBubblerAndVoteType(bubbler, VoteType.DOWNVOTE);
                upvote = reviewService.countByWaterBubblerAndVoteType(bubbler, VoteType.UPVOTE);
            }

            WaterBubblerDTO waterBubblerDTO = waterBubblerMapper.toDTO(bubbler)
                    .setDownvoteCount(downvote)
                    .setUpvoteCount(upvote)
                    .setFavorite(false)
                    .setReview(null);

            if (bubbler.getId() != null && user != null) {
                Review review = reviewService.findByUserAndWaterBubbler(user, bubbler);
                if (review != null) {
                    ReviewDTO reviewDTO = reviewMapper.toDTO(review);
                    waterBubblerDTO.setReview(reviewDTO);
                }

                List<WaterBubbler> list = user.getFavoriteBubblers()
                        .stream()
                        .filter(b -> b.getId() == bubbler.getId())
                        .toList();
                if (!list.isEmpty()) {
                    waterBubblerDTO.setFavorite(true);
                }
            }
            result.add(waterBubblerDTO);
        }

        return result;
    }

    @GetMapping("/{id}")
    public WaterBubblerDTO getWaterBubblerById(@PathVariable UUID id) {
        WaterBubbler waterBubbler = waterBubblerService.getWaterBubblerById(id);
        return waterBubblerMapper.toDTO(waterBubbler);
    }

    @PostMapping
    public ResponseEntity<WaterBubblerDTO> createWaterBubbler(@RequestBody WaterBubblerDTO bubblerDTO, Authentication authentication) {
        String currentUserEmail = authentication.getName();
        AppUser user = userService.getByEmail(currentUserEmail);

        WaterBubbler entity = waterBubblerMapper.toEntity(bubblerDTO)
                .setUser(user);

        WaterBubbler bubbler = waterBubblerService.saveWaterBubbler(entity);

        if (bubblerDTO.isFavorite()) {
            user.addFavoriteBubbler(bubbler);
            userService.updateUser(user);
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bubbler.getId())
                .toUri();

        return ResponseEntity.created(location).body(waterBubblerMapper.toDTO(bubbler));
    }

    @PutMapping("/{id}")
    public String updateWaterBubbler(@PathVariable UUID id, @RequestBody WaterBubblerDTO updatedBubbler) {
        WaterBubbler entity = waterBubblerMapper.toEntity(updatedBubbler);
        waterBubblerService.updateWaterBubbler(id, entity);
        return "Water Bubbler updated";
    }

    @DeleteMapping
    public String deleteWaterBubbler(@RequestBody WaterBubblerIdsDTO request, Authentication authentication) {
        String currentUserEmail = authentication.getName();

        if (request.getBubblerId() != null) {
            WaterBubbler waterBubbler = waterBubblerService.getWaterBubblerById(request.getBubblerId());
            if (waterBubbler == null) {
                return "Water bubbler not found";
            }

            for (AppUser user : waterBubbler.getUsersWhoFavorited()) {
                user.getFavoriteBubblers().remove(waterBubbler);
            }
            userService.saveAll(waterBubbler.getUsersWhoFavorited());

            for (Photo photo : waterBubbler.getPhotos()) {
                String url = photo.getUrl();
                String fileName = url.substring(url.lastIndexOf('/') + 1);

                String fullPath = picturePath + fileName;
                File file = new File(fullPath);
                if (file.exists()) {
                    file.delete();
                }

                photoService.deletePhoto(photo.getId());
            }

            if (waterBubbler.getUser().getEmail().equals(currentUserEmail) ||
                    authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                waterBubblerService.deleteWaterBubbler(request.getBubblerId());
                return "Water bubbler deleted";
            }
            return "Unauthorized to delete this water bubbler";

        } else if (request.getOpenStreetId() != null) {
            Optional<WaterBubbler> waterBubbler = waterBubblerService.getWatterBubblerByOpenStreetId(request.getOpenStreetId());
            if (waterBubbler.isEmpty()) {
                return "Water bubbler not found";
            }

            if (waterBubbler.get().getUser().getEmail().equals(currentUserEmail) ||
                    authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                waterBubblerService.deleteWaterBubblerByOpenStreetId(request.getOpenStreetId());
                return "Water bubbler deleted";
            }
            return "Unauthorized to delete this water bubbler";
        }

        // Žádný platný identifikátor nebyl poskytnut
        return "Invalid request: neither ID nor OpenStreetId provided.";
    }


    @GetMapping("/user")
    public List<WaterBubblerDTO> getWaterBubblersCreatedByUser(Authentication authentication) {
        String currentUserEmail = authentication.getName();
        AppUser user = userService.getByEmail(currentUserEmail);

        return waterBubblerService.getWaterBubblersByUser(user)
                .stream()
                .map(waterBubblerMapper::toDTO)
                .toList();
    }
}
