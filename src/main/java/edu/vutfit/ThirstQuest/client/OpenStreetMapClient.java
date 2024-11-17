package edu.vutfit.ThirstQuest.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.vutfit.ThirstQuest.model.Photo;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class OpenStreetMapClient {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Fetch WaterBubbler data from OpenStreetMap within given coordinates.
     *
     * @param minLon Minimum longitude
     * @param maxLon Maximum longitude
     * @param minLat Minimum latitude
     * @param maxLat Maximum latitude
     * @return List of WaterBubbler objects
     */
    public List<WaterBubbler> fetchWaterBubblers(double minLon, double maxLon, double minLat, double maxLat) {
        String url = String.format(Locale.US, "https://overpass-api.de/api/interpreter?data=[out:json];node[\"amenity\"=\"drinking_water\"](%f,%f,%f,%f);out body;", minLat, minLon, maxLat, maxLon);
        String response = restTemplate.getForObject(url, String.class);

        List<WaterBubbler> waterBubblers = new ArrayList<WaterBubbler>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            JsonNode elements = root.path("elements");

            for (JsonNode element : elements) {
                waterBubblers.add(getWaterBubblerFromOsmElement(element));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return waterBubblers;
    }

    /**
     * Fetch a single WaterBubbler by its OpenStreetMap ID.
     *
     * @param osmId OpenStreetMap ID of the node
     * @return WaterBubbler object
     */
    public WaterBubbler fetchWaterBubblerByOsmId(long osmId) {
        String url = String.format(Locale.US, "https://overpass-api.de/api/interpreter?data=[out:json];node(%d);out body;", osmId);
        String response = restTemplate.getForObject(url, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            JsonNode elements = root.path("elements");

            if (!elements.isEmpty()) {
                return getWaterBubblerFromOsmElement(elements.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Return null if no data is found or in case of error
    }

    private WaterBubbler getWaterBubblerFromOsmElement(JsonNode element) {
        double lat = element.path("lat").asDouble();
        double lon = element.path("lon").asDouble();
        long id = element.path("id").asLong();

        String name = element.path("tags").path("name").asText(null);
        String desc = element.path("tags").path("description").asText(null);
        String imageUrl = element.path("tags").path("image").asText(null);
        String wikimediaCommons = element.path("tags").path("wikimedia_commons").asText(null);

        WaterBubbler waterBubbler = new WaterBubbler()
                .setLatitude(lat)
                .setLongitude(lon)
                .setOpenStreetId(id)
                .setName(name)
                .setDesc(desc)
                .setPhotos(new ArrayList<>());

        if (imageUrl != null) {
            waterBubbler.getPhotos().add(new Photo() {{
                setUrl(imageUrl);
            }});
        }

        if (wikimediaCommons != null) {
            waterBubbler.getPhotos().add(new Photo() {{
                setUrl(getWikimediaImageUrl(wikimediaCommons));
            }});
        }
        return waterBubbler;
    }


    private String getWikimediaImageUrl(String wikimediaCommons) {
        // Construct the URL to the Wikimedia Commons image
        return String.format("https://commons.wikimedia.org/wiki/Special:FilePath/%s", wikimediaCommons.replace(" ", "_"));
    }
}
