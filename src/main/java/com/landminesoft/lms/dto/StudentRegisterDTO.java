package com.landminesoft.lms.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegisterDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Password is required")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$",
        message = "Password must be 8+ chars with 1 uppercase, 1 number, 1 special character"
    )
    private String password;

    @NotBlank(message = "Branch is required")
    private String branch;

    @NotNull(message = "Enrollment year is required")
    private Integer enrollmentYear;
}