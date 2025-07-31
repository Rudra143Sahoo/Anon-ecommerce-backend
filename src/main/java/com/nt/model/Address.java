package com.nt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String mobileNumber;
    private String pincode;
    private String house;
    private String area;
    private String city;
    private String state;
    private String addressType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore // ✅ Prevent circular reference
    private User user;

    // Getters and setters
}
