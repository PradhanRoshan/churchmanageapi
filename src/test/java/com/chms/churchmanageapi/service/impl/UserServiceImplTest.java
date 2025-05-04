package com.chms.churchmanageapi.service.impl;

import com.chms.churchmanageapi.domain.ApplicationStatus;
import com.chms.churchmanageapi.domain.ApplicationStatusHistory;
import com.chms.churchmanageapi.domain.Member;
import com.chms.churchmanageapi.domain.User;
import com.chms.churchmanageapi.dto.SignUpDTO;
import com.chms.churchmanageapi.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserServiceImpl class focusing on user registration functionality
 */
@SpringBootTest
public class UserServiceImplTest {

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_FIRST_NAME = "Test";
    private static final String TEST_LAST_NAME = "User";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String SUCCESS_MESSAGE = "User registered successfully";
    private static final String USERNAME_CONFLICT_MESSAGE = "Username is already in use";

    @Autowired
    private UserServiceImpl userService;

    // User related repositories
    @MockBean private UserRepository userRepository;
    @MockBean private UserRoleRepository userRoleRepository;
    @MockBean private RoleRepository roleRepository;
    
    // Member related repositories
    @MockBean private MemberRepository memberRepository;
    @MockBean private ApplicationStatusRepository applicationStatusRepository;
    @MockBean private ApplicationStatusHistoryRepository applicationStatusHistoryRepository;
    
    @MockBean private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("User registration should succeed with valid input")
    void registerUser_WithValidInput_ShouldSucceed() {
        // Arrange
        SignUpDTO signUpDTO = createValidSignUpDTO();
        setupSuccessfulRegistrationMocks();

        // Act
        String result = userService.registerUser(signUpDTO);

        // Assert
        assertEquals(SUCCESS_MESSAGE, result);
        verifySuccessfulRegistrationCalls();
    }

    @Test
    @DisplayName("User registration should fail when username already exists")
    void registerUser_WithExistingUsername_ShouldFail() {
        // Arrange
        SignUpDTO signUpDTO = createValidSignUpDTO();
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(new User()));

        // Act
        String result = userService.registerUser(signUpDTO);

        // Assert
        assertEquals(USERNAME_CONFLICT_MESSAGE, result);
        verifyNoRegistrationCalls();
    }

    @Test
    @DisplayName("User registration should throw exception when application status not found")
    void registerUser_WithMissingApplicationStatus_ShouldThrowException() {
        // Arrange
        SignUpDTO signUpDTO = createValidSignUpDTO();
        setupMissingApplicationStatusMocks();

        // Act & Assert
        assertThrows(Exception.class, () -> userService.registerUser(signUpDTO));
        verify(userRepository).save(any(User.class));
        verify(memberRepository, never()).save(any(Member.class));
    }

    private SignUpDTO createValidSignUpDTO() {
        User user = new User();
        user.setUsername(TEST_USERNAME);
        user.setPassword(TEST_PASSWORD);
        user.setEmail(TEST_EMAIL);

        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setUser(user);
        signUpDTO.setFirstName(TEST_FIRST_NAME);
        signUpDTO.setLastName(TEST_LAST_NAME);
        return signUpDTO;
    }

    private void setupSuccessfulRegistrationMocks() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(applicationStatusRepository.findById(any())).thenReturn(Optional.of(new ApplicationStatus()));
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(memberRepository.save(any(Member.class))).thenReturn(new Member());
    }

    private void setupMissingApplicationStatusMocks() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(applicationStatusRepository.findById(any())).thenReturn(Optional.empty());
    }

    private void verifySuccessfulRegistrationCalls() {
        verify(userRepository).save(any(User.class));
        verify(memberRepository).save(any(Member.class));
        verify(applicationStatusHistoryRepository).save(any(ApplicationStatusHistory.class));
    }

    private void verifyNoRegistrationCalls() {
        verify(userRepository, never()).save(any(User.class));
        verify(memberRepository, never()).save(any(Member.class));
    }
}