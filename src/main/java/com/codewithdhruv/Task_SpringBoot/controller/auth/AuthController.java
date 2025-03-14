package com.codewithdhruv.Task_SpringBoot.controller.auth;

import com.codewithdhruv.Task_SpringBoot.Dto.AuthenticationRequest;
import com.codewithdhruv.Task_SpringBoot.Dto.AuthenticationResponse;
import com.codewithdhruv.Task_SpringBoot.Dto.SignupRequest;
import com.codewithdhruv.Task_SpringBoot.Dto.UserDto;
import com.codewithdhruv.Task_SpringBoot.entities.User;
import com.codewithdhruv.Task_SpringBoot.repositories.UserRepository;
import com.codewithdhruv.Task_SpringBoot.service.auth.AuthService;
import com.codewithdhruv.Task_SpringBoot.service.jwt.UserService;
import com.codewithdhruv.Task_SpringBoot.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody SignupRequest signupRequest) {
        if(authService.hasUserWithEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exist with this email");
        }
        UserDto createdUserDto=authService.signupUser(signupRequest);
        if(createdUserDto==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    @PostMapping("/login")
    public AuthenticationResponse loginUser(@RequestBody AuthenticationRequest authenticationRequest) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
        } catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect username or password");
        }
        final UserDetails userDetails=userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser=userRepository.findFirstByEmail(authenticationRequest.getEmail());
        final String jwtToken=jwtUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse=new AuthenticationResponse();
        if(optionalUser.isPresent()){
            authenticationResponse.setJwt(jwtToken);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticationResponse;
    }

}
