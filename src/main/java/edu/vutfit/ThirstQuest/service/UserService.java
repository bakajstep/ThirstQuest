package edu.vutfit.ThirstQuest.service;

import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import edu.vutfit.ThirstQuest.repository.AppUserRepository;
import edu.vutfit.ThirstQuest.repository.WaterBubblerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private WaterBubblerRepository watterBubblerRepository;

    @Autowired
    private WaterBubblerOSMService waterBubblerOsmService;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Map the integer role to the correct Spring Security role
        String role = mapRoleToAuthority(user.getRole());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(role)// Set the appropriate role
                .build();
    }

    public AppUser saveUser(AppUser user) {
        if (user.getId() == null && appUserRepository.existsByEmailOrUsername(user.getEmail(), user.getUsername())) {
            return null;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Assign role 2 (USER) by default, or set it during registration
        user.setRole(2); // USER role by default
        return appUserRepository.save(user);
    }

    public AppUser saveAdmin(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Assign role 1 (ADMIN)
        user.setRole(1);
        return appUserRepository.save(user);
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public AppUser getByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public boolean userExists(String email, String username) {
        return appUserRepository.existsByEmailOrUsername(email, username);
    }

    public AppUser getUserById(UUID id) {
        return appUserRepository.findById(id).orElse(null);
    }

    public void deleteUser(UUID id) {
        appUserRepository.deleteById(id);
    }

    // Helper function to map integer roles to Spring Security roles
    private String mapRoleToAuthority(int role) {
        switch (role) {
            case 1:
                return "ADMIN"; // 1 -> ADMIN
            case 2:
                return "USER";  // 2 -> USER
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    public void addFavoriteBubbler(AppUser user, UUID bubblerId, Long osmID) {
        WaterBubbler bubbler = null;
        if (bubblerId != null) {
            Optional<WaterBubbler> tmpBubbler = watterBubblerRepository.findById(bubblerId);
            bubbler = tmpBubbler.orElse(null);
        } else if (osmID != null) {
            WaterBubbler waterBubblerByOsm = waterBubblerOsmService.getWaterBubblerByOsmIdFromOsm(osmID);
            if (waterBubblerByOsm == null) {
                throw new IllegalArgumentException("WaterBubbler does not exist in OpenStreetMap");
            }
            bubbler = watterBubblerRepository.save(waterBubblerByOsm);
        }

        if (bubbler == null) {
            throw new IllegalArgumentException("Adding WaterBubbler to favourrite failed, because buubler not exist.");
        }

        user.getFavoriteBubblers().add(bubbler);
        appUserRepository.save(user);
    }

    public void removeFavoriteBubbler(AppUser user, UUID fountainId) {
        Optional<WaterBubbler> bubbler = watterBubblerRepository.findById(fountainId);

        if (bubbler.isEmpty()) {
            throw new IllegalArgumentException("WaterBubbler not found");
        }

        user.getFavoriteBubblers().remove(bubbler.get());
        appUserRepository.save(user);
    }

    public void removeFavoriteBubblerByOpenStreetId(AppUser user, Long openStreetId) {
        Optional<WaterBubbler> bubbler = watterBubblerRepository.findByOpenStreetId(openStreetId);

        if (bubbler.isEmpty()) {
            throw new IllegalArgumentException("WaterBubbler not found");
        }

        user.getFavoriteBubblers().remove(bubbler.get());
        appUserRepository.save(user);
    }
}
