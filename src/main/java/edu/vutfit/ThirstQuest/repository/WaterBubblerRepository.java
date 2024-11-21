package edu.vutfit.ThirstQuest.repository;

import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface WaterBubblerRepository extends JpaRepository<WaterBubbler, UUID> {
    List<WaterBubbler> findByUser(AppUser appUser);

    List<WaterBubbler> findByLongitudeBetweenAndLatitudeBetween(double minLon, double maxLon, double minLat, double maxLat);

    @Query("SELECT wb FROM WaterBubbler wb JOIN wb.usersWhoFavorited u ON wb.id = u.fountain_id WHERE u = :user AND wb.longitude BETWEEN :minLon AND :maxLon AND wb.latitude BETWEEN :minLat AND :maxLat")
    List<WaterBubbler> findFavoriteBubblersInBBox(double minLon, double maxLon, double minLat, double maxLat, AppUser appUser);

    WaterBubbler findByOpenStreetId(Long openStreetId);
}
