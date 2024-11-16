package edu.vutfit.ThirstQuest.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userService;

    @Bean
    @Lazy
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(c -> c.configurationSource(request -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.setAllowedOriginPatterns(List.of("*"));
                    cors.setAllowedMethods(
                        List.of(
                            HttpMethod.GET.name(),
                            HttpMethod.POST.name(),
                            HttpMethod.PUT.name(),
                            HttpMethod.DELETE.name(),
                            HttpMethod.OPTIONS.name()
                        )
                    );
                    cors.setAllowedHeaders(List.of("*"));
                    cors.setAllowCredentials(true);
                    return cors;
                }))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/status", "/api/auth/register", "/api/auth/login", "/api/auth/google").permitAll()
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(userService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
