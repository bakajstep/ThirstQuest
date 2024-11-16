package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.Review;
import edu.vutfit.ThirstQuest.service.ReviewService;
import edu.vutfit.ThirstQuest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> createReview(@RequestBody Review review, Authentication authentication) {
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

    @PutMapping("/{id}")
    public ResponseEntity<String> updateReview(@PathVariable UUID id, @RequestBody Review updatedReview, Authentication authentication) {
        Review existingReview = reviewService.getReviewById(id);
        String currentUserEmail = authentication.getName();

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
    @DeleteMapping("/{id}")
    public String deleteReview(@PathVariable UUID id, Authentication authentication) {
        Review review = reviewService.getReviewById(id);
        String currentUserEmail = authentication.getName();

        if (review == null) {
            return "Review not found";
        }

        // Only allow the owner of the review or an ADMIN to delete it
        if (review.getUser().getEmail().equals(currentUserEmail) || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            reviewService.deleteReview(id);
            return "Review deleted";
        }

        return "Unauthorized to delete this review";
    }
}
