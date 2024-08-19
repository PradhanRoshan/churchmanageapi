package com.chms.churchmanageapi.service;

import com.chms.churchmanageapi.dto.AuthRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    String registerUser(AuthRequestDTO registerRequest);
}
