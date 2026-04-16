package com.landminesoft.lms.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String message;
}