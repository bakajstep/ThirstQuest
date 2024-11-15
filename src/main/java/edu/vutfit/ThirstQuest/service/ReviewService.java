package edu.vutfit.ThirstQuest.service;

import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.Review;
import edu.vutfit.ThirstQuest.model.VoteType;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import edu.vutfit.ThirstQuest.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review getReviewById(UUID id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public void deleteReview(UUID id) {
        reviewRepository.deleteById(id);
    }

    public Review updateReview(UUID id, Review updatedReview) {
        return reviewRepository.findById(id)
                .map(review -> {
                    review.setVoteType(updatedReview.getVoteType());
                    return reviewRepository.save(review);
                }).orElse(null);
    }

    public Review findByUserAndWaterBubbler(AppUser user, WaterBubbler waterBubbler) {
        return reviewRepository.findByUserAndWaterBubbler(user, waterBubbler);
    }
     public int countByWaterBubblerAndVoteType(WaterBubbler waterBubbler, VoteType voteType) {
        return reviewRepository.countByWaterBubblerAndVoteType(waterBubbler, voteType);
     }
}

