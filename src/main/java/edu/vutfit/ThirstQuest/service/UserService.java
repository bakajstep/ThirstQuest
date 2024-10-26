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

    public void addFavoriteBubbler(UUID userId, UUID fountainId) {
        Optional<AppUser> user = appUserRepository.findById(userId);
        Optional<WaterBubbler> bubbler = watterBubblerRepository.findById(fountainId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        if (bubbler.isEmpty()) {
            throw new IllegalArgumentException("WaterBubbler not found");
        }

        user.get().getFavoriteBubblers().add(bubbler.get());
        appUserRepository.save(user.get());
    }

    public void removeFavoriteBubbler(UUID userId, UUID fountainId) {
        Optional<AppUser> user = appUserRepository.findById(userId);
        Optional<WaterBubbler> bubbler = watterBubblerRepository.findById(fountainId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        if (bubbler.isEmpty()) {
            throw new IllegalArgumentException("WaterBubbler not found");
        }

        user.get().getFavoriteBubblers().remove(bubbler.get());
        appUserRepository.save(user.get());
    }
}
