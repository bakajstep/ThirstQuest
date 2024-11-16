package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.config.JwtTokenUtil;
import edu.vutfit.ThirstQuest.dto.AuthRequest;
import edu.vutfit.ThirstQuest.dto.AuthResponse;
import edu.vutfit.ThirstQuest.dto.GoogleAuthRequest;
import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.service.UserService;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) throws AuthenticationException {
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();

        return new AuthResponse(
            JwtTokenUtil.generateToken(userDetails.getUsername()),
            "Bearer",
            userService.getByEmail(userDetails.getUsername()),
            userDetails.getAuthorities().stream().map(a -> a.getAuthority()).toArray(String[]::new)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AppUser appUser) {
        var user = userService.saveUser(appUser);
        if (user == null) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        var userDetails = userService.loadUserByUsername(user.getEmail());

        return ResponseEntity.ok(new AuthResponse(
            JwtTokenUtil.generateToken(user.getEmail()),
            "Bearer",
            user,
            userDetails.getAuthorities().stream().map(a -> a.getAuthority()).toArray(String[]::new)
        ));
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleSignIn(@RequestBody GoogleAuthRequest authRequest) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList("1090609015078-cj1qrnmmun2j32nequ4q3tvgm6ut6rdf.apps.googleusercontent.com"))
                .build();

            GoogleIdToken idToken = verifier.verify(authRequest.getIdToken());

            if (idToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID token");
            }

            Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String)payload.get("name");
            String photo = (String)payload.get("picture");

            if (!emailVerified) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email not verified");
            }

            AppUser user = userService.getByEmail(email);
            if (user == null) {
                user = new AppUser();
                user.setEmail(email);
                user.setUsername(name);
                user.setPassword("");
                user.setAuthByGoogle(true);
                if (photo != null) {
                    user.setProfilePicture(photo);
                }
                userService.saveUser(user);
            }

            if (!user.isAuthByGoogle()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User already exists and is not authenticated by Google");
            }

            UserDetails userDetails = userService.loadUserByUsername(email);
            String token = JwtTokenUtil.generateToken(email);

            return ResponseEntity.ok(new AuthResponse(
                token,
                "Bearer",
                user,
                userDetails.getAuthorities().stream().map(a -> a.getAuthority()).toArray(String[]::new)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while verifying the ID token");
        }
    }
}
