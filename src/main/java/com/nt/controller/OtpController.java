package com.nt.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nt.model.User;
import com.nt.service.OtpService;
import com.nt.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(HttpServletRequest request) {
    	System.out.println("otp send method hit by frontend");
        // Step 1: Extract userId from JWT (via filter)
        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized: Missing or invalid token");
        }

        // Step 2: Find user from DB
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        String email = user.getEmail();
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User email not found");
        }

        // Step 3: Generate and send OTP to that email
        boolean sent = otpService.sendOtpToEmail(email);
        if (!sent) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP");
        }

        return ResponseEntity.ok("OTP sent successfully to " + email);
    }
    
    
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        String enteredOtp = requestBody.get("otp");

        if (enteredOtp == null || enteredOtp.isBlank()) {
            return ResponseEntity.badRequest().body("OTP is required");
        }

        // Extract userId from JWT (populated by JWT filter)
        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized: Missing userId from token");
        }

        // Get email using userId
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        String email = user.getEmail();  // Get email from DB

        boolean isOtpValid = otpService.verifyOtp(email, enteredOtp);

        if (isOtpValid) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.status(400).body("Invalid or expired OTP");
        }
    }
    
}
