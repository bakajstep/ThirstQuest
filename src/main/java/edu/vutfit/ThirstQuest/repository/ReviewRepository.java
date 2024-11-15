package edu.vutfit.ThirstQuest.repository;

import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.Review;
import edu.vutfit.ThirstQuest.model.VoteType;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Review findByUserAndWaterBubbler(AppUser user, WaterBubbler waterBubbler);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.waterBubbler = :waterBubbler AND r.voteType = :voteType")
    int countByWaterBubblerAndVoteType(@Param("waterBubbler") WaterBubbler waterBubbler, @Param("voteType") VoteType voteType);

}
