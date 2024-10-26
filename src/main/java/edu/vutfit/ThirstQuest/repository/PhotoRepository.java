package edu.vutfit.ThirstQuest.repository;

import edu.vutfit.ThirstQuest.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhotoRepository extends JpaRepository<Photo, UUID> {
}
