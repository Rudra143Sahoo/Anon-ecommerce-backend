package com.nt.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nt.security.JwtUtil;
import com.nt.model.User;
import com.nt.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;  // ✅ This line is missing


    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody User user) {
        
        

        try {
            User createdUser = userService.register(user);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during registration: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String mobile = loginData.get("mobile");
        String password = loginData.get("password");

        Optional<User> userOptional = userService.login(mobile, password);

        if (userOptional.isPresent()) {
        	User user = userOptional.get();
            String token = jwtUtil.generateToken(user.getMobile(), user.getId());
            System.out.println("JWT Token generated: " + token);

            return ResponseEntity.ok(Map.of(
                "token", token,
                "userId", user.getId(),
                "username", user.getName()
            ));
           

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
