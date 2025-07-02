package com.cps.AdminController;

import com.cps.user.User;
import com.cps.profile.UserRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
//@CrossOrigin("*")
public class AdminController {
	@Autowired
	private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepo;

    // Get all pending users
    @GetMapping("/pending-users")
    public List<User> getPendingUsers() {
        return userRepo.findByStatus("PENDING");
    }
    @GetMapping("/expired-users")
    public ResponseEntity<?> getExpiredUsers() {
        LocalDate today = LocalDate.now();
        List<User> expiredUsers = userRepo.findByStatusAndExpiryDateBefore("APPROVED", today);
        return ResponseEntity.ok(expiredUsers);
    }
    
 // Get user details by email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email) {
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", "User not found"));
        }
        return ResponseEntity.ok(user.get()); // ✅ .get() after checking isPresent()
    }

    // Approve a user
    @PostMapping("/approve/{id}")
    public ResponseEntity<Map<String, String>> approveUser(@PathVariable("id") Long id) {
        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "User not found"));
        }

        user.setStatus("APPROVED");
        user.setPayment_status("UNPAID");
        user.setExpiryDate(LocalDate.now().plusDays(7)); // ✅ Example: 1 year access after payment

        // Send approval email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Account Approved 🎉");
        mailMessage.setText("Hi " + user.getName() + ",\n\nYour account has been approved. Your access is valid until " + user.getExpiryDate() + ".\n\nThanks,\nCPS Team");
        mailSender.send(mailMessage);

        userRepo.save(user);
        return ResponseEntity.ok(Collections.singletonMap("message", "User approved successfully"));
    }

    @PostMapping("/payment-request/{email}")
    public ResponseEntity<?> requestPayment(@PathVariable("email") String email) {
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "User not found"));
        }

        User user = userOpt.get();
        user.setPayment_status("REQUESTED");  // ✅ Correct field
        userRepo.save(user);

        return ResponseEntity.ok(Collections.singletonMap("message", "Payment request submitted"));
    }


    @GetMapping("/payment-requests")
    public ResponseEntity<List<User>> getPaymentRequestedUsers() {
        List<User> users = userRepo.findByStatus("REQUESTED");
        return ResponseEntity.ok(users);
    }

    // Delete a user
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userRepo.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
