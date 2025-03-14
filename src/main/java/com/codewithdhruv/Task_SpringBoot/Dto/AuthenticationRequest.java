package com.codewithdhruv.Task_SpringBoot.Dto;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String email;
    private String password;
}
