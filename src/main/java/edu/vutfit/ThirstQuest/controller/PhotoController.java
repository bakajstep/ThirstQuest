package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.model.Photo;
import edu.vutfit.ThirstQuest.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping
    public Photo createPhoto(@RequestBody Photo photo) {
        return photoService.savePhoto(photo);
    }

    @PutMapping("/{id}")
    public Photo updatePhoto(@PathVariable UUID id, @RequestBody Photo updatedPhoto) {
        return photoService.updatePhoto(id, updatedPhoto);
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

