package edu.vutfit.ThirstQuest.service;

import edu.vutfit.ThirstQuest.model.Photo;
import edu.vutfit.ThirstQuest.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public Photo savePhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public void deletePhoto(UUID id) {
        photoRepository.deleteById(id);
    }

    public Photo getPhotoById(UUID id) {
        return photoRepository.findById(id).orElse(null);
    }

    public Photo updatePhoto(UUID id, Photo updatedPhoto) {
        return photoRepository.findById(id)
                .map(photo -> {
                    photo.setName(updatedPhoto.getName());
                    photo.setUrl(updatedPhoto.getUrl());
                    return photoRepository.save(photo);
                }).orElse(null);
    }
}
