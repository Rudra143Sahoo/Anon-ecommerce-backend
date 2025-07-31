package com.nt.dto;


import lombok.Data;


@Data
public class PaymentDTO {
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
    private String status; // e.g., "success" or "failed"

    // Getters & Setters
}
