package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        AppUser appUser = userService.findByEmail(email);
        if (appUser == null) {
            return "User not found";
        }
        if (passwordEncoder.matches(password, appUser.getPassword())) {
            System.out.println("Logged in");
            return "Login successful";
        } else {
            System.out.println("Failed");
            return "Invalid email or password";
        }
    }

    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser appUser) {
        return userService.saveUser(appUser);
    }
}
