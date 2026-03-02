package com.chms.churchmanageapi.service.impl;

import com.chms.churchmanageapi.config.AppConstantsUtil;
import com.chms.churchmanageapi.config.JwtUtil;
import com.chms.churchmanageapi.domain.*;
import com.chms.churchmanageapi.dto.*;
import com.chms.churchmanageapi.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private MemberRepository memberRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private UserRoleRepository userRoleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private ApplicationStatusRepository applicationStatusRepository;
    @Mock private ApplicationStatusHistoryRepository applicationStatusHistoryRepository;
    @Mock private JwtUtil jwtUtil;
    @Mock private UserDetailsService userDetailsService;

    @Captor private ArgumentCaptor<User> userCaptor;
    @Captor private ArgumentCaptor<Member> memberCaptor;
    @Captor private ArgumentCaptor<UserRole> userRoleCaptor;
    @Captor private ArgumentCaptor<ApplicationStatusHistory> applicationStatusHistoryCaptor;

    private UserServiceImpl sut;

    @BeforeEach
    void setUp() {
        // Arrange: constructor-injected SUT (no reflection, no Spring)
        sut = new UserServiceImpl(
                userRepository,
                memberRepository,
                roleRepository,
                userRoleRepository,
                passwordEncoder,
                authenticationManager,
                applicationStatusRepository,
                applicationStatusHistoryRepository,
                jwtUtil,
                userDetailsService
        );
    }

    @Test
    void registerUser_shouldReturnConflictMessage_whenUsernameAlreadyExists() {
        // Arrange
        SignUpDTO dto = signUpDto("alice", "pw", "a@a.com", "Alice", "Doe");
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(new User()));

        // Act
        String result = sut.registerUser(dto);

        // Assert
        assertEquals("Username is already in use", result);
        verify(userRepository).findByUsername("alice");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(memberRepository, userRoleRepository, applicationStatusRepository, applicationStatusHistoryRepository, passwordEncoder);
    }

    @Test
    void registerUser_shouldReturnConflictMessage_whenEmailAlreadyExists() {
        // Arrange
        SignUpDTO dto = signUpDto("alice", "pw", "a@a.com", "Alice", "Doe");
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.of(new User()));

        // Act
        String result = sut.registerUser(dto);

        // Assert
        assertEquals("Email is already in use", result);
        verify(userRepository).findByUsername("alice");
        verify(userRepository).findByEmail("a@a.com");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(memberRepository, userRoleRepository, applicationStatusRepository, applicationStatusHistoryRepository, passwordEncoder);
    }

    @Test
    void registerUser_shouldRegisterSuccessfully_whenInputIsValidAndEmailIsNull() {
        // Arrange
        SignUpDTO dto = signUpDto("alice", "pw", null, "Alice", "Doe");
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());

        when(passwordEncoder.encode("pw")).thenReturn("encoded");

        User savedUser = new User();
        savedUser.setUserId(10L);
        savedUser.setUsername("alice");
        savedUser.setEmail(null);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        ApplicationStatus submitted = new ApplicationStatus();
        submitted.setId(AppConstantsUtil.APPL_STS_SUBMITTED);
        submitted.setStatusName("Submitted");
        when(applicationStatusRepository.findById(AppConstantsUtil.APPL_STS_SUBMITTED)).thenReturn(Optional.of(submitted));
        when(applicationStatusRepository.findById(submitted.getId())).thenReturn(Optional.of(submitted));

        // Act
        String result = sut.registerUser(dto);

        // Assert
        assertEquals("User registered successfully", result);

        verify(userRepository).findByUsername("alice");
        verify(passwordEncoder).encode("pw");

        verify(userRepository).save(userCaptor.capture());
        assertEquals("encoded", userCaptor.getValue().getPassword());

        verify(userRoleRepository).save(userRoleCaptor.capture());
        assertEquals(10L, userRoleCaptor.getValue().getId().getUserId());
        assertEquals(AppConstantsUtil.MEMBER_ROLE_ID, userRoleCaptor.getValue().getId().getRoleId());

        verify(memberRepository).save(memberCaptor.capture());
        assertEquals("MEM-10", memberCaptor.getValue().getMemberId());
        assertEquals("Alice", memberCaptor.getValue().getFirstName());
        assertEquals("Doe", memberCaptor.getValue().getLastName());
        assertNull(memberCaptor.getValue().getEmailId());
        assertSame(savedUser, memberCaptor.getValue().getUser());
        assertSame(submitted, memberCaptor.getValue().getApplicationStatus());

        verify(applicationStatusHistoryRepository).save(applicationStatusHistoryCaptor.capture());
        assertSame(memberCaptor.getValue(), applicationStatusHistoryCaptor.getValue().getMember());
        assertEquals(AppConstantsUtil.APPLICATION_TYPE, applicationStatusHistoryCaptor.getValue().getApplicationType());
        assertEquals(AppConstantsUtil.APPLICATION_COMMENT, applicationStatusHistoryCaptor.getValue().getComment());

        verifyNoMoreInteractions(memberRepository, userRoleRepository, applicationStatusRepository, applicationStatusHistoryRepository);
        verifyNoInteractions(roleRepository);
    }

    @Test
    void registerUser_shouldThrow_whenSubmittedStatusMissing() {
        // Arrange
        SignUpDTO dto = signUpDto("alice", "pw", "a@a.com", "Alice", "Doe");
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pw")).thenReturn("encoded");

        User savedUser = new User();
        savedUser.setUserId(10L);
        savedUser.setUsername("alice");
        savedUser.setEmail("a@a.com");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        when(applicationStatusRepository.findById(AppConstantsUtil.APPL_STS_SUBMITTED)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class, () -> sut.registerUser(dto));

        verify(userRepository).save(any(User.class));
        verify(userRoleRepository).save(any(UserRole.class));
        verify(applicationStatusRepository).findById(AppConstantsUtil.APPL_STS_SUBMITTED);
        verifyNoInteractions(memberRepository, applicationStatusHistoryRepository);
    }

    @Test
    void resetUserPassword_shouldReturnUserNotFound_whenUserDoesNotExist() {
        // Arrange
        ResetPasswordDTO dto = new ResetPasswordDTO("alice", "old", "new", null);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());

        // Act
        String result = sut.resetUserPassword(dto);

        // Assert
        assertEquals("User not found", result);
        verify(userRepository).findByUsername("alice");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void resetUserPassword_shouldReturnIncorrectCurrentPassword_whenCurrentPasswordProvidedAndDoesNotMatch() {
        // Arrange
        ResetPasswordDTO dto = new ResetPasswordDTO("alice", "wrong", "new", null);
        User existing = new User();
        existing.setUsername("alice");
        existing.setPassword("encodedOld");

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(existing));
        when(passwordEncoder.matches("wrong", "encodedOld")).thenReturn(false);

        // Act
        String result = sut.resetUserPassword(dto);

        // Assert
        assertEquals("Incorrect current password", result);
        verify(userRepository).findByUsername("alice");
        verify(passwordEncoder).matches("wrong", "encodedOld");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void resetUserPassword_shouldChangePassword_whenCurrentPasswordIsNull() {
        // Arrange
        ResetPasswordDTO dto = new ResetPasswordDTO("alice", null, "new", null);
        User existing = new User();
        existing.setUsername("alice");
        existing.setPassword("encodedOld");

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("new")).thenReturn("encodedNew");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        String result = sut.resetUserPassword(dto);

        // Assert
        assertEquals("Password changed successfully", result);
        verify(passwordEncoder).encode("new");
        verify(userRepository).save(existing);
        assertEquals("encodedNew", existing.getPassword());
    }

    @Test
    void resetUserPassword_shouldChangePassword_whenCurrentPasswordMatches() {
        // Arrange
        ResetPasswordDTO dto = new ResetPasswordDTO("alice", "old", "new", null);
        User existing = new User();
        existing.setUsername("alice");
        existing.setPassword("encodedOld");

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(existing));
        when(passwordEncoder.matches("old", "encodedOld")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("encodedNew");

        // Act
        String result = sut.resetUserPassword(dto);

        // Assert
        assertEquals("Password changed successfully", result);
        verify(passwordEncoder).matches("old", "encodedOld");
        verify(passwordEncoder).encode("new");
        verify(userRepository).save(existing);
    }

    @Test
    void getUserDetails_shouldReturnNull_whenMemberDoesNotExist() {
        // Arrange
        when(memberRepository.findById("MEM-1")).thenReturn(Optional.empty());

        // Act
        UserDetialsDto result = sut.getUserDetails("MEM-1");

        // Assert
        assertNull(result);
        verify(memberRepository).findById("MEM-1");
        verifyNoMoreInteractions(memberRepository);
        verifyNoInteractions(userRoleRepository, applicationStatusRepository);
    }

    @Test
    void getUserDetails_shouldIncludeAddress_whenMemberHasAddress() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        user.setUsername("alice");
        user.setEmail("a@a.com");

        ApplicationStatus status = new ApplicationStatus();
        status.setId(1L);
        status.setStatusName("Submitted");

        Address address = new Address();
        address.setIdAddr(100L);
        address.setStreet("Main");
        address.setCity("NYC");

        Member member = new Member();
        member.setMemberId("MEM-1");
        member.setFirstName("Alice");
        member.setLastName("Doe");
        member.setEmailId("a@a.com");
        member.setUser(user);
        member.setApplicationStatus(status);
        member.setAddress(address);

        Role role = new Role();
        role.setRoleId(2L);
        role.setRoleName("MEMBER");

        UserRole userRole = new UserRole();
        UserRolePK pk = new UserRolePK();
        pk.setUserId(1L);
        pk.setRoleId(2L);
        userRole.setId(pk);
        userRole.setRole(role);

        when(memberRepository.findById("MEM-1")).thenReturn(Optional.of(member));
        when(userRoleRepository.findByIdUserIdAndUserRoleExptnIsNull(1L)).thenReturn(Optional.of(userRole));

        // Act
        UserDetialsDto result = sut.getUserDetails("MEM-1");

        // Assert
        assertNotNull(result);
        assertNotNull(result.getUser());
        assertNotNull(result.getMember());
        assertNotNull(result.getAddress());
        assertNotNull(result.getRole());
        assertEquals("alice", result.getUser().getUsername());
        assertEquals("MEM-1", result.getMember().getMemberId());
        assertEquals(2L, result.getRole().getRoleId());

        verify(memberRepository).findById("MEM-1");
        verify(userRoleRepository).findByIdUserIdAndUserRoleExptnIsNull(1L);
    }

    @Test
    void userLoginAuthentication_shouldReturnLoginResponse_whenCredentialsValid() {
        // Arrange
        AuthRequestDTO auth = new AuthRequestDTO("alice", "pw", null, null);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("alice");

        when(userDetailsService.loadUserByUsername("alice")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("jwt");
        when(jwtUtil.getExpirationTime()).thenReturn(1234L);

        Role role = new Role();
        role.setRoleId(2L);
        role.setRoleName("MEMBER");

        User user = new User();
        user.setUserId(1L);
        user.setUsername("alice");
        user.setEmail("a@a.com");
        user.setRoles(List.of(role));

        ApplicationStatus status = new ApplicationStatus();
        status.setId(1L);
        status.setStatusName("Submitted");

        Member member = new Member();
        member.setMemberId("MEM-1");
        member.setFirstName("Alice");
        member.setLastName("Doe");
        member.setEmailId("a@a.com");
        member.setUser(user);
        member.setApplicationStatus(status);
        user.setMember(member);

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        UserRole userRole = new UserRole();
        userRole.setRole(role);
        when(userRoleRepository.findByIdUserIdAndUserRoleExptnIsNull(1L)).thenReturn(Optional.of(userRole));

        // Act
        ResponseEntity<LoginResponseDTO> response = sut.userLoginAuthentication(auth);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("jwt", response.getBody().getToken());
        assertEquals(2L, response.getBody().getUserRole());
        assertEquals(1234L, response.getBody().getExpiresIn());
        assertNotNull(response.getBody().getUserDetialsDto());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername("alice");
        verify(jwtUtil).generateToken(userDetails);
        verify(userRepository, times(2)).findByUsername("alice");
        verify(userRoleRepository).findByIdUserIdAndUserRoleExptnIsNull(1L);
    }

    @Test
    void userLoginAuthentication_shouldPropagateException_whenAuthenticationFails() {
        // Arrange
        AuthRequestDTO auth = new AuthRequestDTO("alice", "pw", null, null);
        RuntimeException authEx = new RuntimeException("bad creds");
        doThrow(authEx).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act + Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> sut.userLoginAuthentication(auth));
        assertSame(authEx, thrown);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userDetailsService, jwtUtil, userRepository);
    }

    private static SignUpDTO signUpDto(String username, String password, String email, String firstName, String lastName) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        SignUpDTO dto = new SignUpDTO();
        dto.setUser(user);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        return dto;
    }
}
