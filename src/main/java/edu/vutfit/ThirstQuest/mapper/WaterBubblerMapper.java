package edu.vutfit.ThirstQuest.mapper;

import edu.vutfit.ThirstQuest.dto.WaterBubblerDTO;
import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import edu.vutfit.ThirstQuest.service.UserService;
import edu.vutfit.ThirstQuest.service.WaterBubblerOSMService;
import edu.vutfit.ThirstQuest.service.WaterBubblerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class WaterBubblerMapper {

    private final UserService userService;
    private final WaterBubblerService waterBubblerService;
    private final WaterBubblerOSMService waterBubblerServiceOsm;
    private final PhotoMapper photoMapper;

    @Autowired
    public WaterBubblerMapper(UserService userService, WaterBubblerService waterBubblerService, WaterBubblerOSMService waterBubblerServiceOsm, PhotoMapper photoMapper) {
        this.userService = userService;
        this.waterBubblerService = waterBubblerService;
        this.waterBubblerServiceOsm = waterBubblerServiceOsm;
        this.photoMapper = photoMapper;
    }

    public WaterBubbler toEntity(WaterBubblerDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("WaterBubblerDTO cannot be null");
        }

        WaterBubbler waterBubbler = new WaterBubbler();
        waterBubbler.setId(dto.getId());
        waterBubbler.setName(dto.getName());
        waterBubbler.setLatitude(dto.getLatitude());
        waterBubbler.setLongitude(dto.getLongitude());
        waterBubbler.setDesc(dto.getDescription());


        if (dto.getUserId() != null) {
            AppUser user = userService.getUserById(dto.getUserId());
            if (user == null) {
                throw new IllegalArgumentException("User does not exist");
            }
            waterBubbler.setUser(user);
        }

        return waterBubbler;
    }

    public WaterBubblerDTO toDTO(WaterBubbler entity) {
        if (entity == null) {
            return null;
        }

        WaterBubblerDTO dto = new WaterBubblerDTO();
        dto.setId(entity.getId());
        dto.setOsmId(entity.getOpenStreetId());
        dto.setName(entity.getName());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setDescription(entity.getDesc());

        dto.setPhotos(entity.getPhotos().stream()
            .map(photoMapper::toDTO)
            .collect(Collectors.toList()));

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }

        return dto;
    }
}
