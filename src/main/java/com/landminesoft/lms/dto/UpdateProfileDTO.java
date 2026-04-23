package com.landminesoft.lms.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDTO {
    private String phone;
    private String address;
    private String city;
    private String pincode;
    private LocalDate dob;
}