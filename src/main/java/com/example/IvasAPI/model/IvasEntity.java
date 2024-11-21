package com.example.IvasAPI.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Random;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IvasEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

//    @NotBlank(message = "Company name is required")
//    @Size(min = 2, max = 50, message = "Company name must be between 2 and 50 characters")
//    @Column(nullable = false)
//    private String companyName;

    @Column(nullable = false, unique = true,updatable = false)
    private String binNumber;

    @NotBlank(message = "National ID (NID) is required")
    @Column(nullable = false)
    @Pattern(regexp = "^(\\d{10}|\\d{13}|\\d{18})$", message = "NID must be 10, 13, or 18 digits")
    private String nid;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+880\\d{8}$", message = "Invalid phone number")
    @Column(nullable = false)
    private String phoneNumber;

    @NotBlank(message = "Area of service is required")
    @Column(nullable = false)
    private String areaOfService;

    @Schema(hidden = true)
    private boolean isActive = true;

    @PrePersist
    public void generateBinNumber() {
        if (this.binNumber == null) {
            long timestamp = System.currentTimeMillis() % 100000000000L;
            int random = new Random().nextInt(10);
            this.binNumber = String.format("%012d%d", timestamp, random);
        }
    }
}
