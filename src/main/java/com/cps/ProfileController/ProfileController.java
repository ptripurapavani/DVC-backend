package com.cps.ProfileController;

import com.cps.profile.Profile;
import com.cps.profile.ProfileRepository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
//@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private ProfileRepository repository;

    private static final String UPLOAD_DIR = "uploads/";

    // 1️⃣ CREATE PROFILE
    @PostMapping("/save")
    public ResponseEntity<?> saveProfile(
            @RequestParam("name") String name,
            @RequestParam(value = "profilePic", required = false) MultipartFile profilePic,
            @RequestParam("dob") String dob,
            @RequestParam("qualification") String qualification,
            @RequestParam("designation") String designation,
            @RequestParam("currentCompany") String currentCompany,
            @RequestParam("companyDesignation") String companyDesignation,
            @RequestParam(value = "companyLogo", required = false) MultipartFile companyLogo,
            @RequestParam("companyContact") String companyContact,
            @RequestParam("companyEmail") String companyEmail,
            @RequestParam("companyAddress") String companyAddress,
            @RequestParam("companyLocation") String companyLocation,
            @RequestParam("primaryNumber") String primaryNumber,
            @RequestParam("alternateNumber") String alternateNumber,
            @RequestParam(value = "link1", required = false) String link1,
            @RequestParam(value = "link2", required = false) String link2,
            @RequestParam(value = "link3", required = false) String link3,
            @RequestParam(value = "link4", required = false) String link4,
            @RequestParam(value = "link5", required = false) String link5,
            @RequestParam(value = "link6", required = false) String link6,
            @RequestParam(value = "link7", required = false) String link7,
            @RequestParam(value = "link8", required = false) String link8
    ) {
        try {
            // ❌ Prevent duplicate by email
            if (repository.findByCompanyEmail(companyEmail).isPresent()) {
                return ResponseEntity.badRequest().body("❌ Profile already exists with this company email.");
            }

            Profile profile = buildProfileFromRequest(null, name, profilePic, dob, qualification, designation,
                    currentCompany, companyDesignation, companyLogo, companyContact, companyEmail,
                    companyAddress, companyLocation, primaryNumber, alternateNumber,
                    link1, link2, link3, link4, link5, link6, link7, link8);

            Profile saved = repository.save(profile);
            return ResponseEntity.ok("✅ Profile saved successfully with ID: " + saved.getId());

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("❌ File upload failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ Error saving profile: " + e.getMessage());
        }
    }

    // 2️⃣ FETCH PROFILE BY EMAIL
    @GetMapping("/{email}")
    public ResponseEntity<Profile> getProfile(@PathVariable("email") String email) {
        Optional<Profile> profile = repository.findByCompanyEmail(email);
        return profile.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3️⃣ CHECK PROFILE EXISTS
    @GetMapping("/check/{email}")
    public ResponseEntity<Boolean> checkProfileExists(@PathVariable("email") String email) {
        boolean exists = repository.existsByCompanyEmail(email);
        return ResponseEntity.ok(exists);
    }

    // 4️⃣ UPDATE PROFILE
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProfile(
            @PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "profilePic", required = false) MultipartFile profilePic,
            @RequestParam("dob") String dob,
            @RequestParam("qualification") String qualification,
            @RequestParam("designation") String designation,
            @RequestParam("currentCompany") String currentCompany,
            @RequestParam("companyDesignation") String companyDesignation,
            @RequestParam(value = "companyLogo", required = false) MultipartFile companyLogo,
            @RequestParam("companyContact") String companyContact,
            @RequestParam("companyEmail") String companyEmail,
            @RequestParam("companyAddress") String companyAddress,
            @RequestParam("companyLocation") String companyLocation,
            @RequestParam("primaryNumber") String primaryNumber,
            @RequestParam("alternateNumber") String alternateNumber,
            @RequestParam(value = "link1", required = false) String link1,
            @RequestParam(value = "link2", required = false) String link2,
            @RequestParam(value = "link3", required = false) String link3,
            @RequestParam(value = "link4", required = false) String link4,
            @RequestParam(value = "link5", required = false) String link5,
            @RequestParam(value = "link6", required = false) String link6,
            @RequestParam(value = "link7", required = false) String link7,
            @RequestParam(value = "link8", required = false) String link8
    ) {
        try {
            Optional<Profile> optionalProfile = repository.findById(id);
            if (optionalProfile.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // ✅ Check if email belongs to another profile
            Optional<Profile> existingEmailProfile = repository.findByCompanyEmail(companyEmail);
            if (existingEmailProfile.isPresent() && !existingEmailProfile.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body("❌ Another profile already uses this email.");
            }

            Profile existingProfile = optionalProfile.get();
            Profile updatedProfile = buildProfileFromRequest(existingProfile, name, profilePic, dob, qualification, designation,
                    currentCompany, companyDesignation, companyLogo, companyContact, companyEmail,
                    companyAddress, companyLocation, primaryNumber, alternateNumber,
                    link1, link2, link3, link4, link5, link6, link7, link8);

            repository.save(updatedProfile);
            return ResponseEntity.ok("✅ Profile updated successfully!");

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("❌ File upload failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ Error updating profile: " + e.getMessage());
        }
    }

    // 🔧 Utility: Build Profile from Request
    private Profile buildProfileFromRequest(Profile profile, String name, MultipartFile profilePic, String dob,
                                            String qualification, String designation, String currentCompany,
                                            String companyDesignation, MultipartFile companyLogo, String companyContact,
                                            String companyEmail, String companyAddress, String companyLocation,
                                            String primaryNumber, String alternateNumber,
                                            String link1, String link2, String link3, String link4,
                                            String link5, String link6, String link7, String link8) throws IOException {

        if (profile == null) profile = new Profile();

        profile.setName(name);
        profile.setDob(dob);
        profile.setQualification(qualification);
        profile.setDesignation(designation);
        profile.setCompanyName(currentCompany);
        profile.setCompanyDesignation(companyDesignation);
        profile.setCompanyPhone(companyContact);
        profile.setCompanyEmail(companyEmail);
        profile.setCompanyAddress(companyAddress);
        profile.setCompanyLocation(companyLocation);
        profile.setPrimaryPhone(primaryNumber);
        profile.setAlternatePhone(alternateNumber);

        profile.setLink1(link1);
        profile.setLink2(link2);
        profile.setLink3(link3);
        profile.setLink4(link4);
        profile.setLink5(link5);
        profile.setLink6(link6);
        profile.setLink7(link7);
        profile.setLink8(link8);

        if (profilePic != null && !profilePic.isEmpty()) {
            String profilePicName = saveFile(profilePic);
            profile.setProfilePicUrl(profilePicName);
        }

        if (companyLogo != null && !companyLogo.isEmpty()) {
            String logoName = saveFile(companyLogo);
            profile.setCompanyLogoUrl(logoName);
        }

        return profile;
    }

    // 📁 Save uploaded file to disk
    private String saveFile(MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getExtension(originalFilename);

        if (!extension.matches("(?i)jpg|jpeg|png")) {
            throw new IOException("Only JPG, JPEG, or PNG files are allowed.");
        }

        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return UPLOAD_DIR + fileName;
    }

    // 🔍 Get file extension
    private String getExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return (lastDot == -1) ? "" : filename.substring(lastDot + 1);
    }
}
