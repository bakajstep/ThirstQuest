package edu.vutfit.ThirstQuest.config;

import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserInitializer implements CommandLineRunner {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if the default user already exists
        if (appUserRepository.findByEmail("admin@admin.com") == null) {
            // Create the default admin user
            AppUser defaultUser = new AppUser();
            defaultUser.setEmail("admin@admin.com");
            defaultUser.setUsername("admin");
            defaultUser.setRole(1);
            defaultUser.setPassword(passwordEncoder.encode("admin123")); // Set a default password (hashed)

            appUserRepository.save(defaultUser);

            System.out.println("Default admin user created: admin@admin.com with password 'admin123'");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
