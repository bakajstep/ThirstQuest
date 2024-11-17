package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.dto.PhotoDTO;
import edu.vutfit.ThirstQuest.mapper.PhotoMapper;
import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.Photo;
import edu.vutfit.ThirstQuest.service.PhotoService;
import edu.vutfit.ThirstQuest.service.UserService;
import edu.vutfit.ThirstQuest.service.WaterBubblerOSMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private UserService userService;

    @PostMapping
    public String createPhoto(@RequestBody PhotoDTO dto, Authentication authentication) {
        Photo photo = photoMapper.toEntity(dto);
        String currentUserEmail = authentication.getName();

        AppUser user = userService.getByEmail(currentUserEmail);
        photo.setUser(user);

        photoService.savePhoto(photo);
        return "Photo Created";
    }

    @PutMapping("/{id}")
    public String updatePhoto(@PathVariable UUID id, @RequestBody PhotoDTO updatedPhoto, Authentication authentication) {
        Photo entity = photoMapper.toEntity(updatedPhoto);
        String currentUserEmail = authentication.getName();

        AppUser user = userService.getByEmail(currentUserEmail);
        entity.setUser(user);

        photoService.updatePhoto(id, entity);
        return "Photo Updated";
    }

    @DeleteMapping("/{id}")
    public String deletePhoto(@PathVariable UUID id, Authentication authentication) {
        Photo photo = photoService.getPhotoById(id);
        String currentUserEmail = authentication.getName();

        if (photo == null) {
            return "Review not found";
        }

        // Only allow the owner of the photo or an ADMIN to delete it
        if (photo.getUser().getEmail().equals(currentUserEmail) || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            photoService.deletePhoto(id);
            return "Photo deleted";
        }

        return "Unauthorized to delete this photo";
    }
}

