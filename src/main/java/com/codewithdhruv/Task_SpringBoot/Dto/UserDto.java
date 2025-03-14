package com.codewithdhruv.Task_SpringBoot.Dto;

import com.codewithdhruv.Task_SpringBoot.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String password;
    private UserRole userRole;
}
