package com.codewithdhruv.Task_SpringBoot.Dto;

import com.codewithdhruv.Task_SpringBoot.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private String jwt;
    private Long userId;
    private UserRole userRole;
}
