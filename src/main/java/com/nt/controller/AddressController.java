package com.nt.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nt.model.Address;
import com.nt.model.User;
import com.nt.service.AddressService;
import com.nt.service.UserService;

@RestController
@RequestMapping("/api/address")

public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    // ✅ Add new address for logged-in user using JWT
    @PostMapping("/add")
    public ResponseEntity<?> addAddress(@RequestBody Address address, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");  // From JWT Filter

        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid token or user not found");
        }

        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        address.setUser(user);
        Address savedAddress = addressService.saveAddress(address);
        return ResponseEntity.ok(savedAddress);
    }

    // ✅ Get all addresses for logged-in user (no path variable)
    @GetMapping("/user")
    public ResponseEntity<?> getUserAddresses(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        System.out.println("USER ID from JWT: " + userId);
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid token");
        }

        List<Address> addresses = addressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(addresses);
    }

    // ✅ Update address for authenticated user (optional user check)
    @PutMapping("/update")
    public ResponseEntity<?> updateAddress(@RequestBody Address address, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid token");
        }

        // Optional: Check if the address belongs to the user
        Address existing = addressService.findById(address.getId());
        if (existing == null || !existing.getUser().getId().equals(userId)) {
            return ResponseEntity.status(403).body("You can only update your own address");
        }

        address.setUser(existing.getUser()); // preserve user
        Address updated = addressService.updateAddress(address);
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete address for authenticated user
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        Address existing = addressService.findById(id);
        if (existing == null || !existing.getUser().getId().equals(userId)) {
            return ResponseEntity.status(403).body("You can only delete your own address");
        }

        addressService.deleteAddress(id);
        return ResponseEntity.ok("Address deleted successfully");
    }
}
