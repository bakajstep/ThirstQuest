package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.dto.WaterBubblerIdsDTO;
import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/favorites")
    public ResponseEntity<String> addFavoriteFountain(@RequestBody WaterBubblerIdsDTO bubbler, Authentication authentication) {
        String currentUserEmail = authentication.getName();
        AppUser user = userService.getByEmail(currentUserEmail);
        try {
            userService.addFavoriteBubbler(user, bubbler.getBubblerId(), bubbler.getOpenStreetId());
            return ResponseEntity.ok("WaterBubbler was added to favorites.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/favorites")
    public ResponseEntity<String> removeFavoriteFountain(
            @RequestBody WaterBubblerIdsDTO request,
            Authentication authentication) {
        String currentUserEmail = authentication.getName();
        AppUser user = userService.getByEmail(currentUserEmail);

        try {
            if (request.getBubblerId() != null) {
                userService.removeFavoriteBubbler(user, request.getBubblerId());
            } else if (request.getOpenStreetId() != null) {
                userService.removeFavoriteBubblerByOpenStreetId(user, request.getOpenStreetId());
            } else {
                return ResponseEntity.badRequest().body("No valid identifier provided for WaterBubbler.");
            }

            return ResponseEntity.ok("WaterBubbler was removed from favorites.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

