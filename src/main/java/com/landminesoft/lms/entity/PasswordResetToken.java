package com.landminesoft.lms.entity;

import lombok.Builder;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String userType; // STUDENT, FACULTY, ADMIN

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @Builder.Default
    private Boolean used = false;
}