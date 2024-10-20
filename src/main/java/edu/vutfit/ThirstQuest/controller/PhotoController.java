package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.model.Photo;
import edu.vutfit.ThirstQuest.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void deletePhoto(@PathVariable UUID id) {
        photoService.deletePhoto(id);
    }
}

