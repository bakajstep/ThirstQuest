package edu.vutfit.ThirstQuest.repository;

import edu.vutfit.ThirstQuest.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    AppUser findByEmail(String email);

    boolean existsByEmailOrUsername(String email, String username);
}
