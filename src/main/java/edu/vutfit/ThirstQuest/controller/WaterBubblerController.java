package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.dto.WaterBubblerDTO;
import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.Review;
import edu.vutfit.ThirstQuest.model.VoteType;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import edu.vutfit.ThirstQuest.repository.WaterBubblerRepository;
import edu.vutfit.ThirstQuest.service.ReviewService;
import edu.vutfit.ThirstQuest.service.UserService;
import edu.vutfit.ThirstQuest.service.WaterBubblerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

    @GetMapping
    public List<WaterBubbler> getAllWaterBubblers(
        @RequestParam double minLat,
        @RequestParam double maxLat,
        @RequestParam double minLon,
        @RequestParam double maxLon
    ) {
        //List<WaterBubblerDTO> result = new ArrayList<>();
        return waterBubblerService.getWaterBubblersWithinCoordinates(minLon, maxLon, minLat, maxLat);

        // for (WaterBubbler bubbler : bubblers) {
        //     int downvote = reviewService.countByWaterBubblerAndVoteType(bubbler, VoteType.DOWNVOTE);
        //     int upvote = reviewService.countByWaterBubblerAndVoteType(bubbler, VoteType.UPVOTE);

        //     result.add(new WaterBubblerDTO(bubbler)
        //             .setDownvoteCount(downvote)
        //             .setUpvoteCount(upvote));
        // }

        // return result;
    }

    @GetMapping("/{id}")
    public WaterBubbler getWaterBubblerById(@PathVariable UUID id) {
        return waterBubblerService.getWaterBubblerById(id);
    }

    @PostMapping
    public WaterBubbler createWaterBubbler(@RequestBody WaterBubbler waterBubbler) {
        return waterBubblerService.saveWaterBubbler(waterBubbler);
    }

    @PutMapping("/{id}")
    public WaterBubbler updateWaterBubbler(@PathVariable UUID id, @RequestBody WaterBubbler updatedBubbler) {
        return waterBubblerService.updateWaterBubbler(id, updatedBubbler);
    }

    @DeleteMapping("/{id}")
    public String deleteWaterBubbler(@PathVariable UUID id, Authentication authentication) {
        WaterBubbler waterBubbler = waterBubblerService.getWaterBubblerById(id);
        String currentUserEmail = authentication.getName();

        if (waterBubbler == null) {
            return "Water bubbler not found";
        }

        // Only allow the owner of the water bubbler or an ADMIN to delete it
        if (waterBubbler.getUser().getEmail().equals(currentUserEmail) || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            waterBubblerService.deleteWaterBubbler(id);
            return "Water bubbler deleted";
        }
        return "Unauthorized to delete this water bubbler";
    }

    @GetMapping("/user/{userId}")
    public List<WaterBubbler> getWaterBubblersByUser(@PathVariable UUID userId) {
        AppUser appUser = userService.getUserById(userId);
        return waterBubblerService.getWaterBubblersByUser(appUser);
    }
}
