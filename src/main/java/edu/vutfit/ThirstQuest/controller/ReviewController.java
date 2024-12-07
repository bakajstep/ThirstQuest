package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.dto.ReviewDTO;
import edu.vutfit.ThirstQuest.dto.WaterBubblerIdsDTO;
import edu.vutfit.ThirstQuest.mapper.ReviewMapper;
import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.Review;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import edu.vutfit.ThirstQuest.service.ReviewService;
import edu.vutfit.ThirstQuest.service.UserService;
import edu.vutfit.ThirstQuest.service.WaterBubblerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private WaterBubblerService waterBubblerService;

    @Autowired
    private ReviewMapper reviewMapper;

    @PostMapping
    public ResponseEntity<String> createReview(@RequestBody ReviewDTO dto, Authentication authentication) {
        Review review = reviewMapper.toEntity(dto);
        String currentUserEmail = authentication.getName();

        AppUser user = userService.getByEmail(currentUserEmail);
        review.setUser(user);

        Review existingReview = reviewService.findByUserAndWaterBubbler(user, review.getWaterBubbler());
        if (existingReview != null) {
            return ResponseEntity.badRequest().body("Review already exists. Use PUT to update.");
        }

        reviewService.saveReview(review);
        return ResponseEntity.ok("Review created");
    }

    @PutMapping
    public ResponseEntity<String> updateReview(@RequestBody ReviewDTO updatedReview, Authentication authentication) {
        String currentUserEmail = authentication.getName();

        Review existingReview = null;

        if (updatedReview.getWaterBubblerId() != null) {
            existingReview = reviewService.findByUserEmailAndWaterBubblerId(currentUserEmail, updatedReview.getWaterBubblerId());
        } else if (updatedReview.getWaterBubblerOsmId() != null) {
            AppUser byEmail = userService.getByEmail(currentUserEmail);
            Optional<WaterBubbler> watterBubblerByOpenStreetId = waterBubblerService.getWatterBubblerByOpenStreetId(bubbler.getOpenStreetId());
            if (watterBubblerByOpenStreetId.isPresent()) {
                existingReview = reviewService.findByUserAndWaterBubbler(byEmail, watterBubblerByOpenStreetId.get());
            }
        }


        if (existingReview == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        }

        if (!existingReview.getUser().getEmail().equals(currentUserEmail) &&
                authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to update this review");
        }

        existingReview.setVoteType(updatedReview.getVoteType());
        reviewService.saveReview(existingReview);

        return ResponseEntity.ok("Review updated");
    }

    // Delete review (only the owner or ADMIN can delete)
    @DeleteMapping
    public ResponseEntity<String> deleteReview(@RequestBody WaterBubblerIdsDTO bubbler, Authentication authentication) {
        String currentUserEmail = authentication.getName();

        Review review = null;

        if (bubbler.getBubblerId() != null) {
            review = reviewService.findByUserEmailAndWaterBubblerId(currentUserEmail, bubbler.getBubblerId());

        } else if (bubbler.getOpenStreetId() != null) {
            AppUser byEmail = userService.getByEmail(currentUserEmail);
            Optional<WaterBubbler> watterBubblerByOpenStreetId = waterBubblerService.getWatterBubblerByOpenStreetId(bubbler.getOpenStreetId());
            if (watterBubblerByOpenStreetId.isPresent()) {
                review = reviewService.findByUserAndWaterBubbler(byEmail, watterBubblerByOpenStreetId.get());
            }
        }

        if (review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        }

        // Only allow the owner of the review or an ADMIN to delete it
        if (review.getUser().getEmail().equals(currentUserEmail) || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            reviewService.deleteReview(review.getId());
            return ResponseEntity.ok("Review deleted");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to update this review");
    }
}
