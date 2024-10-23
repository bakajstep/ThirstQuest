package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import edu.vutfit.ThirstQuest.service.UserService;
import edu.vutfit.ThirstQuest.service.WaterBubblerService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public List<WaterBubbler> getAllWaterBubblers(
        @RequestParam double minLat,
        @RequestParam double maxLat,
        @RequestParam double minLon,
        @RequestParam double maxLon
    ) {
        return waterBubblerService.getWaterBubblersWithinCoordinates(minLon, maxLon, minLat, maxLat);
    }

    @GetMapping("/{id}")
    public WaterBubbler getWaterBubblerById(@PathVariable UUID id) {
        return waterBubblerService.getWaterBubblerById(id);
    }

    @PostMapping
    public WaterBubbler createWaterBubbler(@RequestBody WaterBubbler waterBubbler) {
        return waterBubblerService.saveWaterBubbler(waterBubbler);
    }

    @PutMapping("/{id}")
    public WaterBubbler updateWaterBubbler(@PathVariable UUID id, @RequestBody WaterBubbler updatedBubbler) {
        return waterBubblerService.updateWaterBubbler(id, updatedBubbler);
    }

    @DeleteMapping("/{id}")
    public void deleteWaterBubbler(@PathVariable UUID id) {
        waterBubblerService.deleteWaterBubbler(id);
    }

    @GetMapping("/user/{userId}")
    public List<WaterBubbler> getWaterBubblersByUser(@PathVariable UUID userId) {
        AppUser appUser = userService.getUserById(userId);
        return waterBubblerService.getWaterBubblersByUser(appUser);
    }
}
