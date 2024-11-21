package edu.vutfit.ThirstQuest.mapper;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.vutfit.ThirstQuest.dto.FavoriteBubblerDTO;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import edu.vutfit.ThirstQuest.service.WaterBubblerOSMService;
import edu.vutfit.ThirstQuest.service.WaterBubblerService;

@Component
public class FavoriteBubblerMapper {
    private final WaterBubblerService waterBubblerService;
    private final WaterBubblerOSMService waterBubblerServiceOsm;

    @Autowired
    public FavoriteBubblerMapper(WaterBubblerService waterBubblerService, WaterBubblerOSMService waterBubblerServiceOsm) {
        this.waterBubblerService = waterBubblerService;
        this.waterBubblerServiceOsm = waterBubblerServiceOsm;
    }

    public UUID toEntity(FavoriteBubblerDTO dto) {
        if (dto == null) {
            return null;
        }

        if (dto.getWaterBubblerId() != null) {
            WaterBubbler waterBubbler = waterBubblerService.getWaterBubblerById(dto.getWaterBubblerId());
            if (waterBubbler == null) {
                throw new IllegalArgumentException("WaterBubbler does not exist by id");
            }
            return waterBubbler.getId();
        }

        if (dto.getWaterBubblerOsmId() != null) {
            WaterBubbler waterBubblerByOsm = waterBubblerServiceOsm.getWaterBubblerByOsmIdFromOsm(dto.getWaterBubblerOsmId());
            if (waterBubblerByOsm == null) {
                throw new IllegalArgumentException("WaterBubbler does not exist in OpenStreetMap");
            }
            waterBubblerService.saveWaterBubbler(waterBubblerByOsm);
            return waterBubblerByOsm.getId();
        }

        return null;
    }
}
