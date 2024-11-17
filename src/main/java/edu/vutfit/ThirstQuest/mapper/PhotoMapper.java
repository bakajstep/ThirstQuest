package edu.vutfit.ThirstQuest.mapper;

import edu.vutfit.ThirstQuest.dto.PhotoDTO;
import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.Photo;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import edu.vutfit.ThirstQuest.service.UserService;
import edu.vutfit.ThirstQuest.service.WaterBubblerOSMService;
import edu.vutfit.ThirstQuest.service.WaterBubblerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhotoMapper {

    private final UserService userService;
    private final WaterBubblerService waterBubblerService;
    private final WaterBubblerOSMService waterBubblerServiceOsm;

    @Autowired
    public PhotoMapper(UserService userService, WaterBubblerService waterBubblerService, WaterBubblerOSMService waterBubblerServiceOsm) {
        this.userService = userService;
        this.waterBubblerService = waterBubblerService;
        this.waterBubblerServiceOsm = waterBubblerServiceOsm;
    }

    /**
     * Převod PhotoDTO na Photo entitu.
     *
     * @param dto PhotoDTO objekt
     * @return Photo entita
     */
    public Photo toEntity(PhotoDTO dto) {
        if (dto == null) {
            return null;
        }

        Photo photo = new Photo();
        photo.setId(dto.getId());
        photo.setName(dto.getName());
        photo.setUrl(dto.getUrl());

        if (dto.getUserId() != null) {
            AppUser user = userService.getUserById(dto.getUserId());
            photo.setUser(user);
        }

        if (dto.getWaterBubblerId() != null) {
            WaterBubbler waterBubbler = waterBubblerService.getWaterBubblerById(dto.getWaterBubblerId());
            if (waterBubbler == null) {
                throw new IllegalArgumentException("WaterBubbler does not exist by id");
            }
            photo.setWaterBubbler(waterBubbler);
        } else if (dto.getWaterBubblerOsmId() != null) {
            WaterBubbler waterBubblerByOsm = waterBubblerServiceOsm.getWaterBubblerByOsmIdFromOsm(dto.getWaterBubblerOsmId());
            if (waterBubblerByOsm == null) {
                throw new IllegalArgumentException("WaterBubbler does not exist in OpenStreetMap");
            }
            waterBubblerService.saveWaterBubbler(waterBubblerByOsm);
            photo.setWaterBubbler(waterBubblerByOsm);
        }

        return photo;
    }

    /**
     * Převod Photo entity na PhotoDTO.
     *
     * @param entity Photo entita
     * @return PhotoDTO objekt
     */
    public PhotoDTO toDTO(Photo entity) {
        if (entity == null) {
            return null;
        }

        PhotoDTO dto = new PhotoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setUrl(entity.getUrl());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }

        if (entity.getWaterBubbler() != null) {
            dto.setWaterBubblerId(entity.getWaterBubbler().getId());
            dto.setWaterBubblerOsmId(entity.getWaterBubbler().getOpenStreetId());
        }

        return dto;
    }
}
