package edu.vutfit.ThirstQuest.mapper;

import edu.vutfit.ThirstQuest.dto.ReviewDTO;
import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.Review;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import edu.vutfit.ThirstQuest.service.UserService;
import edu.vutfit.ThirstQuest.service.WaterBubblerOSMService;
import edu.vutfit.ThirstQuest.service.WaterBubblerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private final UserService userService;
    private final WaterBubblerService waterBubblerService;
    private final WaterBubblerOSMService waterBubblerServiceOsm;

    @Autowired
    public ReviewMapper(UserService userService, WaterBubblerService waterBubblerService, WaterBubblerOSMService waterBubblerServiceOsm) {
        this.userService = userService;
        this.waterBubblerService = waterBubblerService;
        this.waterBubblerServiceOsm = waterBubblerServiceOsm;
    }

    public Review toEntity(ReviewDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("ReviewDTO cannot be null");
        }

        Review review = new Review();
        review.setId(dto.getId());
        review.setVoteType(dto.getVoteType());

        if (dto.getUserId() != null) {
            AppUser user = userService.getUserById(dto.getUserId());
            if (user == null) {
                throw new IllegalArgumentException("User does not exist");
            }
            review.setUser(user);
        }

        if (dto.getWaterBubblerId() != null) {
            WaterBubbler waterBubbler = waterBubblerService.getWaterBubblerById(dto.getWaterBubblerId());
            if (waterBubbler == null) {
                throw new IllegalArgumentException("WaterBubbler does not exist by id");
            }
            review.setWaterBubbler(waterBubbler);
        } else if (dto.getWaterBubblerOsmId() != null) {
            WaterBubbler waterBubblerByOsm = waterBubblerServiceOsm.getWaterBubblerByOsmIdFromOsm(dto.getWaterBubblerOsmId());
            if (waterBubblerByOsm == null) {
                throw new IllegalArgumentException("WaterBubbler does not exist in OpenStreetMap");
            }
            waterBubblerService.saveWaterBubbler(waterBubblerByOsm);
            review.setWaterBubbler(waterBubblerByOsm);
        }

        return review;
    }

    public ReviewDTO toDTO(Review entity) {
        if (entity == null) {
            return null;
        }

        ReviewDTO dto = new ReviewDTO();
        dto.setId(entity.getId());
        dto.setVoteType(entity.getVoteType());

        if (entity.getWaterBubbler() != null) {
            dto.setWaterBubblerOsmId(entity.getWaterBubbler().getOpenStreetId());
            dto.setWaterBubblerId(entity.getWaterBubbler().getId());
        }

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }

        return dto;
    }
}
