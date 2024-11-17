package edu.vutfit.ThirstQuest.service;

import edu.vutfit.ThirstQuest.client.OpenStreetMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.vutfit.ThirstQuest.model.WaterBubbler;

import java.util.List;

@Service
public class WaterBubblerOSMService {

    @Autowired
    private OpenStreetMapClient openStreetMapClient;

    public List<WaterBubbler> getWaterBubblersWithinCoordinatesFromOsm(double minLon, double maxLon, double minLat, double maxLat) {
        return openStreetMapClient.fetchWaterBubblers(minLon, maxLon, minLat, maxLat);
    }

    public WaterBubbler getWaterBubblerByOsmIdFromOsm(Long osmId) {
        return openStreetMapClient.fetchWaterBubblerByOsmId(osmId);
    }
}
