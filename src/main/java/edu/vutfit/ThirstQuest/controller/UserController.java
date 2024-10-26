package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/{userId}/favorites/{fountainId}")
    public ResponseEntity<String> addFavoriteFountain(@PathVariable UUID userId, @PathVariable UUID fountainId) {
        try {
            userService.addFavoriteBubbler(userId, fountainId);
            return ResponseEntity.ok("WaterBubbler was added to favorites.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/favorites/{fountainId}")
    public ResponseEntity<String> removeFavoriteFountain(@PathVariable UUID userId, @PathVariable UUID fountainId) {
        try {
            userService.removeFavoriteBubbler(userId, fountainId);
            return ResponseEntity.ok("WaterBubbler was removed to favorites.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

