package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.dto.FavoriteBubblerDTO;
import edu.vutfit.ThirstQuest.mapper.FavoriteBubblerMapper;
import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FavoriteBubblerMapper favoriteBubblerMapper;

    @PostMapping("/favorites")
    public ResponseEntity<String> addFavoriteFountain(@RequestBody FavoriteBubblerDTO favoriteBubblerDTO, Authentication authentication) {
        String currentUserEmail = authentication.getName();
        AppUser user = userService.getByEmail(currentUserEmail);
        UUID bubblerId = favoriteBubblerMapper.toEntity(favoriteBubblerDTO);

        try {
            userService.addFavoriteBubbler(user, bubblerId);
            return ResponseEntity.ok("WaterBubbler was added to favorites.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/favorites/{fountainId}")
    public ResponseEntity<String> removeFavoriteFountain(@PathVariable UUID fountainId, Authentication authentication) {
        String currentUserEmail = authentication.getName();
        AppUser user = userService.getByEmail(currentUserEmail);

        try {
            userService.removeFavoriteBubbler(user, fountainId);
            return ResponseEntity.ok("WaterBubbler was removed to favorites.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

