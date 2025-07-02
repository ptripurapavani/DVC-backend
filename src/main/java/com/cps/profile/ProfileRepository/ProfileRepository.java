package com.cps.profile.ProfileRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cps.profile.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByCompanyEmail(String email);
    boolean existsByCompanyEmail(String email);
}

