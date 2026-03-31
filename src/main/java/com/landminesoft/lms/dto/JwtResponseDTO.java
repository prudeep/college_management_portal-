package com.landminesoft.lms.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponseDTO {
    private String token;
    @Builder.Default
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String name;
    private String role;
}