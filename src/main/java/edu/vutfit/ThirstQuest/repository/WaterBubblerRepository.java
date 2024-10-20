package edu.vutfit.ThirstQuest.repository;

import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WaterBubblerRepository extends JpaRepository<WaterBubbler, UUID> {
    List<WaterBubbler> findByUser(AppUser appUser);
}
