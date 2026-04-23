package com.landminesoft.lms.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordDTO {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
}