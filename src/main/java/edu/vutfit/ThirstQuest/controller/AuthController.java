package edu.vutfit.ThirstQuest.controller;

import edu.vutfit.ThirstQuest.config.JwtTokenUtil;
import edu.vutfit.ThirstQuest.dto.AuthRequest;
import edu.vutfit.ThirstQuest.dto.AuthResponse;
import edu.vutfit.ThirstQuest.model.AppUser;
import edu.vutfit.ThirstQuest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public AuthResponse register(@RequestBody AppUser appUser) {
        var user = userService.saveUser(appUser);

        return new AuthResponse(
            JwtTokenUtil.generateToken(user.getEmail()),
            "Bearer",
            user,
            new String[] {}
        );
    }
}
