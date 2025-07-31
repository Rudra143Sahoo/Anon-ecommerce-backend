package com.nt.model;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private String paymentId;
    private String status;

    // Getters & Setters
}
