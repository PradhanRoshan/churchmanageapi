package com.chms.churchmanageapi.controller;

import com.chms.churchmanageapi.config.JwtUtil;
import com.chms.churchmanageapi.dto.AuthRequestDTO;
import com.chms.churchmanageapi.dto.LoginResponseDTO;
import com.chms.churchmanageapi.dto.ResetPasswordDTO;
import com.chms.churchmanageapi.dto.SignUpDTO;
import com.chms.churchmanageapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> createAuthenticationToken(@RequestBody AuthRequestDTO authRequest) throws Exception {

       return userService.userLoginAuthentication(authRequest);
    }

    @PostMapping(value = "/signup", produces = "application/json")
    public String registerUser(@RequestBody SignUpDTO signUpDTO) {
      return  userService.registerUser(signUpDTO);
    }

    @PostMapping(value = "/reset-password", produces = "application/json")
    public String resetUserPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        return  userService.resetUserPassword(resetPasswordDTO);
    }
}
