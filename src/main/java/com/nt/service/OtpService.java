package com.nt.service;

import com.nt.model.Otp;
import com.nt.repo.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OtpRepository otpRepository;

    public boolean sendOtpToEmail(String email) {
        try {
            String otpCode = generateOtp();

            // Expiration = now + 5 minutes
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);

            // Save to database
            Otp otp = new Otp();
            otp.setEmail(email);
            otp.setOtpCode(otpCode);
            otp.setExpirationTime(expirationTime);

            otpRepository.save(otp);

            // Send email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your OTP Code");
            message.setText("Your One-Time Password (OTP) is: " + otpCode + "\n\nThis OTP will expire in 5 minutes.");
            mailSender.send(message);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateOtp() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));  // 6-digit
    }

    // Optional: Verify OTP
    public boolean verifyOtp(String email, String enteredOtp) {
        return otpRepository.findTopByEmailOrderByExpirationTimeDesc(email)
                .filter(otp -> otp.getOtpCode().equals(enteredOtp))
                .filter(otp -> otp.getExpirationTime().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}

