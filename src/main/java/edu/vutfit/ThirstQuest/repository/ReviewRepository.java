package edu.vutfit.ThirstQuest.repository;

import edu.vutfit.ThirstQuest.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
