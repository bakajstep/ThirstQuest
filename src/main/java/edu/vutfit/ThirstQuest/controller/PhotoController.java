package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.dto.ErrorDTO;
import edu.vutfit.ThirstQuest.dto.PhotoDTO;
import edu.vutfit.ThirstQuest.mapper.PhotoMapper;
import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.Photo;
import edu.vutfit.ThirstQuest.service.PhotoService;
import edu.vutfit.ThirstQuest.service.UserService;
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
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final String[] ALLOWED_FILE_TYPES = {"image/jpeg", "image/png", "image/webp"};

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
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "waterBubblerId", required = false) UUID waterBubblerId,
            @RequestParam(value = "waterBubblerOsmId", required = false) Long waterBubblerOsmId,
            Authentication authentication
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorDTO("File is empty"));
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body(new ErrorDTO("File size exceeds 5 MB"));
        }

        String fileType = getFileType(file);
        if (!isValidFileType(fileType)) {
            return ResponseEntity.badRequest()
                    .body(new ErrorDTO("Invalid file type. Only JPEG and PNG are allowed."));
        }

        String currentUserEmail = authentication.getName();
        AppUser user = userService.getByEmail(currentUserEmail);

        try {
            PhotoDTO photoDTO = saveFile(file, user, waterBubblerId, waterBubblerOsmId);
            return ResponseEntity.ok(photoDTO);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDTO("An error occurred while saving the file"));
        }
    }

    @PostMapping("/upload/profile")
    public ResponseEntity<?> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorDTO("File is empty"));
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body(new ErrorDTO("File size exceeds 5 MB"));
        }

//        String fileType = getFileType(file);
//        if (!isValidFileType(fileType)) {
//            return ResponseEntity.badRequest()
//                    .body(new ErrorDTO("Invalid file type. Only JPEG, PNG and WEBP are allowed."));
//        }

        String currentUserEmail = authentication.getName();
        AppUser user = userService.getByEmail(currentUserEmail);

        deleteOldProfilePicture(user);

        try {
            PhotoDTO photoDTO = saveFile(file, user, null, null);
            user.setProfilePicture(photoDTO.getUrl());
            userService.updateUser(user);
            return ResponseEntity.ok(photoDTO);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDTO("An error occurred while saving the profile picture"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePhoto(@PathVariable UUID id, Authentication authentication) {
        Photo photo = photoService.getPhotoById(id);
        String currentUserEmail = authentication.getName();

        if (photo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorDTO("Photo not found"));
        }

        if (photo.getUser().getEmail().equals(currentUserEmail) ||
                authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            deleteFileAsync(photo.getName());
            photoService.deletePhoto(id);
            return ResponseEntity.ok("Photo deleted");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorDTO("Unauthorized to delete this photo"));
    }

    private void deleteOldProfilePicture(AppUser user) {
        if (user.getProfilePicture() != null) {
            deleteFileAsync(user.getProfilePicture());
        }
    }

    private void deleteFileAsync(String fileName) {
        CompletableFuture.runAsync(() -> {
            try {
                Files.deleteIfExists(Paths.get(fileName));
            } catch (IOException e) {}
        });
    }

    private PhotoDTO saveFile(MultipartFile file, AppUser user, UUID waterBubblerId, Long waterBubblerOsmId) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get(uploadDir + File.separator + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        String fileUrl = serverUrl + "/uploads/" + fileName;

        return new PhotoDTO()
                .setId(UUID.randomUUID())
                .setName(fileName)
                .setUrl(fileUrl)
                .setUserId(user.getId())
                .setWaterBubblerId(waterBubblerId)
                .setWaterBubblerOsmId(waterBubblerOsmId);
    }

    private String getFileType(MultipartFile file) {
        return file.getContentType();
    }

    private boolean isValidFileType(String fileType) {
        if (fileType == null) return false;
        for (String allowedType : ALLOWED_FILE_TYPES) {
            if (allowedType.equals(fileType)) {
                return true;
            }
        }
        return false;
    }
}

