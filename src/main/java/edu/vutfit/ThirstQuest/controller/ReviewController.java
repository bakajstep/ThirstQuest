package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.model.Review;
import edu.vutfit.ThirstQuest.service.ReviewService;
import edu.vutfit.ThirstQuest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
