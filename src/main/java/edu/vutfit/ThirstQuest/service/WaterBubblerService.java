package edu.vutfit.ThirstQuest.service;

import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import edu.vutfit.ThirstQuest.repository.WaterBubblerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class WaterBubblerService {

    @Autowired
    private WaterBubblerRepository waterBubblerRepository;

    @Autowired
    private WaterBubblerOSMService waterBubblerOSMService;

    public List<WaterBubbler> getWaterBubblersWithinCoordinates(double minLon, double maxLon, double minLat, double maxLat) {
        List<WaterBubbler> osmWaterBubblers = waterBubblerOSMService.getWaterBubblersWithinCoordinatesFromOsm(minLon, maxLon, minLat, maxLat);
        List<WaterBubbler> waterBubblers = waterBubblerRepository.findByLongitudeBetweenAndLatitudeBetween(minLon, maxLon, minLat, maxLat);

        for (WaterBubbler osmWaterBubbler : osmWaterBubblers) {
            WaterBubbler existing = waterBubblers
                .stream()
                .filter(waterBubbler -> waterBubbler.getOpenStreetId() != null)
                .filter(waterBubbler -> waterBubbler.getOpenStreetId().equals(osmWaterBubbler.getOpenStreetId()))
                .findFirst()
                .orElse(null);

            if (existing == null) {
                waterBubblers.add(osmWaterBubbler);
                continue;
            }

            var photos = existing.getPhotos();
            if (photos == null) {
                existing.setPhotos(new ArrayList<>());
            }

            existing.getPhotos().addAll(osmWaterBubbler.getPhotos());
        }

        return waterBubblers;
    }

    public WaterBubbler getWaterBubblerById(UUID id) {
        return waterBubblerRepository.findById(id).orElse(null);
    }

    public WaterBubbler saveWaterBubbler(WaterBubbler waterBubbler) {
        return waterBubblerRepository.save(waterBubbler);
    }

    public List<WaterBubbler> getWaterBubblersByUser(AppUser appUser) {
        return waterBubblerRepository.findByUser(appUser);
    }

    public void deleteWaterBubbler(UUID id) {
        waterBubblerRepository.deleteById(id);
    }

    public WaterBubbler updateWaterBubbler(UUID id, WaterBubbler updatedBubbler) {
        return waterBubblerRepository.findById(id)
                .map(bubbler -> {
                    bubbler.setName(updatedBubbler.getName());
                    bubbler.setLatitude(updatedBubbler.getLatitude());
                    bubbler.setLongitude(updatedBubbler.getLongitude());
                    bubbler.setDescription(updatedBubbler.getDescription());
                    return waterBubblerRepository.save(bubbler);
                }).orElse(null);
    }

    public Optional<WaterBubbler> getWatterBubblerByOpenStreetId(Long id) {
        return waterBubblerRepository.findByOpenStreetId(id);
    }

    public void deleteWaterBubblerByOpenStreetId(Long id) {
        waterBubblerRepository.deleteByOpenStreetId(id);
    }
}
