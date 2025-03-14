package com.codewithdhruv.Task_SpringBoot.service.auth;

import com.codewithdhruv.Task_SpringBoot.Dto.SignupRequest;
import com.codewithdhruv.Task_SpringBoot.Dto.UserDto;
import com.codewithdhruv.Task_SpringBoot.entities.User;
import com.codewithdhruv.Task_SpringBoot.enums.UserRole;
import com.codewithdhruv.Task_SpringBoot.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @PostConstruct
    public void createAdminAccount(){
        Optional<User> optionalUser=userRepository.findByUserRole(UserRole.ADMIN);
        if(optionalUser.isEmpty()) {
            User user=new User();
            user.setEmail("admin@test.com");
            user.setName("Admin");
            user.setPassword(new BCryptPasswordEncoder().encode("admin1"));
            user.setUserRole(UserRole.ADMIN);
            userRepository.save(user);
            System.out.println("Admin account created successfully");
        } else {
            System.out.println("Admin account already exist!");
        }
    }

    @Override
    public UserDto signupUser(SignupRequest request) {
        User user=new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        user.setUserRole(UserRole.EMPLOYEE);
        User createdUser=userRepository.save(user);
        return createdUser.getUserDto();
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
