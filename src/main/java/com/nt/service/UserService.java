package com.nt.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.model.User;
import com.nt.repo.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        // Check if user already exists with same mobile or email
        if (userRepository.existsByMobile(user.getMobile())) {
            throw new RuntimeException("User with this mobile already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        return userRepository.save(user);
    }

    public Optional<User> login(String mobile, String password) {
        return userRepository.findByMobileAndPassword(mobile, password);
    }
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
