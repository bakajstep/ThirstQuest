package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.dto.WaterBubblerDTO;
import edu.vutfit.ThirstQuest.mapper.WaterBubblerMapper;
import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import edu.vutfit.ThirstQuest.service.UserService;
import edu.vutfit.ThirstQuest.service.WaterBubblerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/waterbubblers")
public class WaterBubblerController {

    @Autowired
    private WaterBubblerService waterBubblerService;

    @Autowired
    private UserService userService;

    @Autowired
    private WaterBubblerMapper waterBubblerMapper;

    @GetMapping
    public List<WaterBubblerDTO> getAllWaterBubblers(
        @RequestParam double minLat,
        @RequestParam double maxLat,
        @RequestParam double minLon,
        @RequestParam double maxLon
    ) {
        List<WaterBubbler> bubblers = waterBubblerService.getWaterBubblersWithinCoordinates(minLon, maxLon, minLat, maxLat);

        return bubblers.stream().map(waterBubblerMapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public WaterBubblerDTO getWaterBubblerById(@PathVariable UUID id) {
        WaterBubbler waterBubbler = waterBubblerService.getWaterBubblerById(id);
        return waterBubblerMapper.toDTO(waterBubbler);
    }

    @PostMapping
    public String createWaterBubbler(@RequestBody WaterBubblerDTO waterBubbler) {
        WaterBubbler entity = waterBubblerMapper.toEntity(waterBubbler);
        waterBubblerService.saveWaterBubbler(entity);
        return "Water Bubbler created";
    }

    @PutMapping("/{id}")
    public String updateWaterBubbler(@PathVariable UUID id, @RequestBody WaterBubblerDTO updatedBubbler) {
        WaterBubbler entity = waterBubblerMapper.toEntity(updatedBubbler);
        waterBubblerService.updateWaterBubbler(id, entity);
        return "Water Bubbler updated";
    }

    @DeleteMapping("/{id}")
    public String deleteWaterBubbler(@PathVariable UUID id, Authentication authentication) {
        WaterBubbler waterBubbler = waterBubblerService.getWaterBubblerById(id);
        String currentUserEmail = authentication.getName();

        if (waterBubbler == null) {
            return "Water bubbler not found";
        }

        // Only allow the owner of the water bubbler or an ADMIN to delete it
        if (waterBubbler.getUser().getEmail().equals(currentUserEmail) || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            waterBubblerService.deleteWaterBubbler(id);
            return "Water bubbler deleted";
        }
        return "Unauthorized to delete this water bubbler";
    }

    @GetMapping("/user/{userId}")
    public List<WaterBubbler> getWaterBubblersByUser(@PathVariable UUID userId) {
        AppUser appUser = userService.getUserById(userId);
        return waterBubblerService.getWaterBubblersByUser(appUser);
    }
}
