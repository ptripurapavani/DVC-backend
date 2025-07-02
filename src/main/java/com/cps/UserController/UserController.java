package com.cps.UserController;

import com.cps.LoginRequest.LoginRequest;
import com.cps.profile.Profile;
import com.cps.profile.ProfileRepository.ProfileRepository;
import com.cps.profile.UserRepository.UserRepository;
import com.cps.user.User;
import com.cps.token.VerificationToken;
import com.cps.token.VerificationTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin("*")
public class UserController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VerificationTokenRepository tokenRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProfileRepository profileRepo;

    // ✅ User Registration
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Check for existing email
   

        Optional<User> existingEmail = userRepo.findByEmail(user.getEmail());
        if (existingEmail.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email already registered"));
        }

        // Check for existing mobile
        Optional<User> existingMobile = userRepo.findByMobile(user.getMobile());
        if (existingMobile.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Mobile number already registered"));
        }

        // Set user status and save
        user.setStatus("PENDING");
        user.setPayment_status("UNPAID"); // ➕ Add this line

       
        user.setEnabled(false);
        user.setRegistrationDate(LocalDate.now());
        user.setExpiryDate(LocalDate.now().plusDays(7));  // ✅ 7-day trial
        user.setPayment_status("UNPAID"); // ➕ Add this line
     // Generate a unique short key (e.g., 6-character)
        String shortKey = UUID.randomUUID().toString().substring(0, 6);
        user.setShortKey(shortKey);  // ✅ Add this line
        userRepo.save(user);
         
        // Generate verification token
        String token = UUID.randomUUID().toString();
        VerificationToken verifyToken = new VerificationToken(token, user);
        tokenRepo.save(verifyToken);

        // Send verification email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Email Verification");
        mailMessage.setText("Click the link to verify your email: http://localhost:8081/api/users/verify?token=" + token);
        mailSender.send(mailMessage);

        return ResponseEntity.ok(Map.of("message", "✅ Registered! Check your email to verify."));
        
    }
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token) {
        Optional<VerificationToken> optionalToken = tokenRepo.findByToken(token);

        if (optionalToken.isPresent()) {
            VerificationToken verificationToken = optionalToken.get();
            User user = verificationToken.getUser();
            user.setEnabled(true);
            userRepo.save(user);
            return "✅ Email verified! Please wait for admin approval before logging in.";
        } else {
            return "❌ Invalid or expired verification token.";
        }
    }

    // ✅ User Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User user = userRepo.findByEmailAndPassword(req.getEmail(), req.getPassword());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }

        if (!"APPROVED".equalsIgnoreCase(user.getStatus())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Account not approved yet. Please wait for admin approval."));
        }
       /* if (user.getExpiryDate() != null && user.getExpiryDate().isBefore(LocalDate.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "❌ Your account has expired. Please contact admin."));
        }*/
       

        Optional<Profile> optionalProfile = profileRepo.findByCompanyEmail(req.getEmail());

        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            return ResponseEntity.ok(Map.of(
                    "status", "PROFILE_EXISTS",
                    "profile", profile
                   
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "status", "NO_PROFILE",
                    "message", "No profile found. Please fill in your profile details."
                      
            ));
        }
    }
    @PutMapping("/extend-validity/{id}")
    public ResponseEntity<?> extendValidity(@PathVariable("id") Long id, @RequestParam("years") int years) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }

        User user = optionalUser.get();
        user.setExpiryDate(LocalDate.now().plusYears(years)); 
        userRepo.save(user);

        // Optional email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("✅ Payment Approved & Validity Extended");
        mailMessage.setText("Hi " + user.getName() + ",\n\nYour payment has been verified and your account is valid until " + user.getExpiryDate() + ".\n\nThanks,\nCPS Team");
        mailSender.send(mailMessage);

        return ResponseEntity.ok(Map.of("message", "✅ Validity extended and payment marked as PAID"));
    }
    @GetMapping("/shortkey/{key}")
    public ResponseEntity<?> getUserByShortKey(@PathVariable("key") String key) {
        Optional<User> user = userRepo.findByShortKey(key);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }
    }



    
}
