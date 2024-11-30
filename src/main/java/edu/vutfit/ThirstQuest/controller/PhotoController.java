package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.dto.PhotoDTO;
import edu.vutfit.ThirstQuest.mapper.PhotoMapper;
import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.Photo;
import edu.vutfit.ThirstQuest.service.PhotoService;
import edu.vutfit.ThirstQuest.service.UserService;
import edu.vutfit.ThirstQuest.service.WaterBubblerOSMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${server.url}")
    private String serverUrl;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<PhotoDTO> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "waterBubblerId", required = false) UUID waterBubblerId,
            @RequestParam(value = "waterBubblerOsmId", required = false) Long waterBubblerOsmId,
            Authentication authentication
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(null);
        }

        String currentUserEmail = authentication.getName();
        AppUser user = userService.getByEmail(currentUserEmail);

        try {
            String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            Path path = Paths.get(uploadDir + File.separator + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            String fileUrl = serverUrl + "/uploads/" + fileName;

            PhotoDTO photoDTO = new PhotoDTO()
                    .setId(UUID.randomUUID())
                    .setName(fileName)
                    .setUrl(fileUrl)
                    .setUserId(user.getId())
                    .setWaterBubblerId(waterBubblerId)
                    .setWaterBubblerOsmId(waterBubblerOsmId);

            Photo photo = photoMapper.toEntity(photoDTO);
            photoService.savePhoto(photo);

            return ResponseEntity.ok(photoDTO);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePhoto(@PathVariable UUID id, Authentication authentication) {
        Photo photo = photoService.getPhotoById(id);
        String currentUserEmail = authentication.getName();

        if (photo == null) {
            return ResponseEntity.status(404).body("Photo not found");
        }

        if (photo.getUser().getEmail().equals(currentUserEmail) ||
                authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            Path filePath = Paths.get(uploadDir, photo.getName());
            File file = filePath.toFile();
            if (file.exists() && !file.delete()) {
                return ResponseEntity.status(500).body("Failed to delete the photo file");
            }

            photoService.deletePhoto(id);

            return ResponseEntity.ok("Photo deleted");
        }

        return ResponseEntity.status(403).body("Unauthorized to delete this photo");
    }
}

