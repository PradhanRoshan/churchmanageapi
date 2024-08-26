package com.chms.churchmanageapi.service;

import com.chms.churchmanageapi.dto.AuthRequestDTO;
import com.chms.churchmanageapi.dto.SignUpDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    String registerUser(SignUpDTO signUpDTO);
}
