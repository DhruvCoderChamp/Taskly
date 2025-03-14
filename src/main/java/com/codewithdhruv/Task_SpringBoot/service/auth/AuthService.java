package com.codewithdhruv.Task_SpringBoot.service.auth;

import com.codewithdhruv.Task_SpringBoot.Dto.SignupRequest;
import com.codewithdhruv.Task_SpringBoot.Dto.UserDto;

public interface AuthService {

    UserDto signupUser(SignupRequest request);
    boolean hasUserWithEmail(String email);
}
