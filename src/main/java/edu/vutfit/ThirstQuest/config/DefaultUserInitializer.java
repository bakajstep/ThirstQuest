package edu.vutfit.ThirstQuest.config;

import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.model.WaterBubbler;
import edu.vutfit.ThirstQuest.repository.AppUserRepository;
import edu.vutfit.ThirstQuest.repository.WaterBubblerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserInitializer implements CommandLineRunner {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private WaterBubblerRepository waterBubblerRepository;

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

        if (waterBubblerRepository.findByUser(appUserRepository.findByEmail("admin@admin.com")).isEmpty()) {
            WaterBubbler waterBubbler = new WaterBubbler();
            waterBubbler.setName("WaterBubbler");
            waterBubbler.setUser(appUserRepository.findByEmail("admin@admin.com"));
            WaterBubbler save = waterBubblerRepository.save(waterBubbler);

            System.out.println("Water Bubbler created with name 'WaterBubbler': " + save.getId());
        }

    }
}
