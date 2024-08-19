package com.chms.churchmanageapi.service.impl;

import com.chms.churchmanageapi.domain.User;
import com.chms.churchmanageapi.dto.AuthRequestDTO;
import com.chms.churchmanageapi.repository.UserRepository;
import com.chms.churchmanageapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public String registerUser(AuthRequestDTO registerRequest) {
        Optional<User> user = userRepository.findByUsername(registerRequest.getUsername());
        if (user.isPresent()) {
            return "Username is already in use";
        }
        User users = new User();
        users.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        users.setUsername(registerRequest.getUsername());
        users.setEmail(registerRequest.getEmail());
        userRepository.save(users);
        return "User registered successfully";
    }
}
