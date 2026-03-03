package com.chms.churchmanageapi.service.impl;

import com.chms.churchmanageapi.config.CacheConfig;
import com.chms.churchmanageapi.domain.ApplicationStatus;
import com.chms.churchmanageapi.domain.Member;
import com.chms.churchmanageapi.domain.User;
import com.chms.churchmanageapi.dto.ApplicationReviewDecisionDTO;
import com.chms.churchmanageapi.dto.ApplicationStatusDto;
import com.chms.churchmanageapi.dto.MemberDto;
import com.chms.churchmanageapi.dto.RegistrationTrackingDTO;
import com.chms.churchmanageapi.dto.RoleDto;
import com.chms.churchmanageapi.dto.SignUpDTO;
import com.chms.churchmanageapi.repository.AddressRepository;
import com.chms.churchmanageapi.repository.ApplicationStatusHistoryRepository;
import com.chms.churchmanageapi.repository.ApplicationStatusRepository;
import com.chms.churchmanageapi.repository.MemberRepository;
import com.chms.churchmanageapi.repository.UserRepository;
import com.chms.churchmanageapi.repository.UserRoleRepository;
import com.chms.churchmanageapi.service.CommentsService;
import com.chms.churchmanageapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class MemberServiceImplCachingTest {

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private CacheManager cacheManager;

    @MockBean private MemberRepository memberRepository;

    // Mock UserService used by MemberServiceImpl for DTO mapping
    @MockBean(name = "userService")
    private UserService userService;

    // Use the real proxied service so @CacheEvict runs.
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService signupService;

    @MockBean private CommentsService commentsService;
    @MockBean private ApplicationStatusRepository applicationStatusRepository;
    @MockBean private UserRoleRepository userRoleRepository;
    @MockBean private ApplicationStatusHistoryRepository applicationStatusHistoryRepository;
    @MockBean private AddressRepository addressRepository;
    @MockBean private UserRepository userRepository;
    @MockBean private PasswordEncoder passwordEncoder;

    @BeforeEach
    void clearCache() {
        assertNotNull(cacheManager.getCache(CacheConfig.CACHE_REGISTRATION_TRACKING_ALL));
        cacheManager.getCache(CacheConfig.CACHE_REGISTRATION_TRACKING_ALL).clear();
    }

    @Test
    void getRegistrationTracking_secondCallShouldHitCache_andNotCallRepositoryAgain() {
        // Arrange
        Member m = new Member();
        m.setMemberId("MEM-1");
        ApplicationStatus st = new ApplicationStatus();
        st.setId(1L);
        st.setStatusName("Submitted");
        m.setApplicationStatus(st);
        User u = new User();
        u.setUserId(10L);
        m.setUser(u);

        when(memberRepository.findAll()).thenReturn(List.of(m));
        when(userService.getMemberDtoDetails(m)).thenReturn(new MemberDto());
        when(userService.getRoleDtoDetails(10L)).thenReturn(new RoleDto(2L, "MEMBER"));
        when(commentsService.getComments("MEM-1")).thenReturn(List.of());

        // Act
        List<RegistrationTrackingDTO> first = memberService.getRegistrationTracking();
        List<RegistrationTrackingDTO> second = memberService.getRegistrationTracking();

        // Assert
        assertNotNull(first);
        assertNotNull(second);
        // same instance often happens for in-memory caches; not required, but a nice sanity check
        assertSame(first, second);

        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void reviewApplicationDecision_shouldEvictRegistrationTrackingCache() {
        // Arrange: prime the cache
        Member m = new Member();
        m.setMemberId("MEM-1");
        ApplicationStatus st = new ApplicationStatus();
        st.setId(1L);
        st.setStatusName("Submitted");
        m.setApplicationStatus(st);
        User u = new User();
        u.setUserId(10L);
        m.setUser(u);

        when(memberRepository.findAll()).thenReturn(List.of(m));
        when(userService.getMemberDtoDetails(m)).thenReturn(new MemberDto());
        when(userService.getRoleDtoDetails(10L)).thenReturn(new RoleDto(2L, "MEMBER"));
        when(commentsService.getComments("MEM-1")).thenReturn(List.of());

        memberService.getRegistrationTracking();
        verify(memberRepository, times(1)).findAll();

        // Set up reviewApplicationDecision path (minimal to pass through without NPE)
        when(memberRepository.findById("MEM-1")).thenReturn(Optional.of(m));

        ApplicationStatus inProgress = new ApplicationStatus();
        inProgress.setId(2L);
        inProgress.setStatusName("In Progress");
        when(applicationStatusRepository.findById(anyLong())).thenReturn(Optional.of(inProgress));

        ApplicationReviewDecisionDTO dto = new ApplicationReviewDecisionDTO(
                "MEM-1",
                new RoleDto(2L, "MEMBER"),
                new ApplicationStatusDto(1L, "Submitted")
        );

        // Act: this should evict the cache via @CacheEvict
        memberService.reviewApplicationDecision(dto);

        // Next call should rebuild from repository
        memberService.getRegistrationTracking();

        // Assert: findAll called twice total (prime + after eviction)
        verify(memberRepository, times(2)).findAll();
    }

    @Test
    void registerUser_shouldEvictRegistrationTrackingCache() {
        // Prime registration tracking cache
        Member m = new Member();
        m.setMemberId("MEM-1");
        ApplicationStatus st = new ApplicationStatus();
        st.setId(1L);
        st.setStatusName("Submitted");
        m.setApplicationStatus(st);
        User u = new User();
        u.setUserId(10L);
        m.setUser(u);

        when(memberRepository.findAll()).thenReturn(List.of(m));
        when(userService.getMemberDtoDetails(m)).thenReturn(new MemberDto());
        when(userService.getRoleDtoDetails(10L)).thenReturn(new RoleDto(2L, "MEMBER"));
        when(commentsService.getComments("MEM-1")).thenReturn(List.of());

        memberService.getRegistrationTracking();
        verify(memberRepository, times(1)).findAll();

        // Sanity: cache was populated
        assertNotNull(cacheManager.getCache(CacheConfig.CACHE_REGISTRATION_TRACKING_ALL).get(org.springframework.cache.interceptor.SimpleKey.EMPTY));

        // Arrange signup mocks (keep it minimal)
        SignUpDTO signUp = new SignUpDTO();
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("pwd");
        signUp.setUser(newUser);
        signUp.setFirstName("New");
        signUp.setLastName("User");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());

        User savedUser = new User();
        savedUser.setUserId(99L);
        savedUser.setUsername("newuser");
        savedUser.setEmail("newuser@example.com");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        ApplicationStatus submitted = new ApplicationStatus();
        submitted.setId(1L);
        when(applicationStatusRepository.findById(anyLong())).thenReturn(Optional.of(submitted));

        // Act: signup should evict cache (via @CacheEvict on UserServiceImpl.registerUser)
        signupService.registerUser(signUp);

        // Assert: cache should be empty after eviction
        assertNull(cacheManager.getCache(CacheConfig.CACHE_REGISTRATION_TRACKING_ALL).get(org.springframework.cache.interceptor.SimpleKey.EMPTY));

        // Next call should rebuild from repository (cache was evicted)
        memberService.getRegistrationTracking();

        verify(memberRepository, times(2)).findAll();
    }
}
