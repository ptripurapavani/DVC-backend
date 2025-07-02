package com.cps.profile.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cps.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByEmailAndPassword(String email,String password);
    Optional<User> findByMobile(String mobile);
    List<User> findByStatus(String status); // <-- Add this
    
    List<User> findByExpiryDateBefore(LocalDate date);
    //List<User> findByStatusAndExpiryDateAfter(String status, LocalDate date);
    
    List<User> findByStatusAndExpiryDateBefore(String status, LocalDate date);
    Optional<User> findByShortKey(String shortKey);

}





