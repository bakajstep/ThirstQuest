package edu.vutfit.ThirstQuest.mapper;

import edu.vutfit.ThirstQuest.dto.AppUserDTO;
import edu.vutfit.ThirstQuest.model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {

    public AppUser toEntity(AppUserDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("AppUserDTO cannot be null");
        }

        AppUser appUser = new AppUser();
        appUser.setId(dto.getId());
        appUser.setEmail(dto.getEmail());
        appUser.setUsername(dto.getUsername());
        appUser.setAuthByGoogle(dto.isAuthByGoogle());
        appUser.setRole(dto.getRole());
        appUser.setProfilePicture(dto.getProfilePicture());
        // Note: Password and other sensitive fields should be handled separately
        return appUser;
    }

    public AppUserDTO toDTO(AppUser entity) {
        if (entity == null) {
            return null;
        }

        return new edu.vutfit.ThirstQuest.dto.AppUserDTO()
                .setId(entity.getId())
                .setEmail(entity.getEmail())
                .setUsername(entity.getUsername())
                .setAuthByGoogle(entity.isAuthByGoogle())
                .setRole(entity.getRole())
                .setProfilePicture(entity.getProfilePicture());
    }
}
