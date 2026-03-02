package com.chms.churchmanageapi.service.impl;

import com.chms.churchmanageapi.config.AppConstantsUtil;
import com.chms.churchmanageapi.domain.*;
import com.chms.churchmanageapi.dto.*;
import com.chms.churchmanageapi.repository.*;
import com.chms.churchmanageapi.service.CommentsService;
import com.chms.churchmanageapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock private MemberRepository memberRepository;
    @Mock private UserService userService;
    @Mock private CommentsService commentsService;
    @Mock private ApplicationStatusRepository applicationStatusRepository;
    @Mock private UserRoleRepository userRoleRepository;
    @Mock private ApplicationStatusHistoryRepository applicationStatusHistoryRepository;
    @Mock private AddressRepository addressRepository;

    @Captor private ArgumentCaptor<Member> memberCaptor;
    @Captor private ArgumentCaptor<UserRole> userRoleCaptor;
    @Captor private ArgumentCaptor<Address> addressCaptor;

    private MemberServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new MemberServiceImpl(
                memberRepository,
                userService,
                commentsService,
                applicationStatusRepository,
                userRoleRepository,
                applicationStatusHistoryRepository,
                addressRepository
        );
    }

    @Test
    void getRegistrationTracking_shouldMapAllMembersAndIncludeAddressAndComments() {
        // Arrange
        Member member = memberWithUserAndStatus("MEM-1", 1L);
        Address address = new Address();
        address.setIdAddr(10L);
        member.setAddress(address);

        when(memberRepository.findAll()).thenReturn(List.of(member));
        when(userService.getMemberDtoDetails(member)).thenReturn(new MemberDto());
        when(userService.getRoleDtoDetails(member.getUser().getUserId())).thenReturn(new RoleDto(2L, "MEMBER"));
        when(userService.getAddressDtoDetails(address)).thenReturn(new AddressDto());
        when(commentsService.getComments("MEM-1")).thenReturn(List.of(new RgstrnRqstCmntDTO()));

        // Act
        List<RegistrationTrackingDTO> result = sut.getRegistrationTracking();

        // Assert
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getUserMember());
        assertNotNull(result.get(0).getRole());
        assertNotNull(result.get(0).getApplicationStatus());
        assertNotNull(result.get(0).getAddress());
        assertNotNull(result.get(0).getComments());

        verify(memberRepository).findAll();
        verify(userService).getMemberDtoDetails(member);
        verify(userService).getRoleDtoDetails(member.getUser().getUserId());
        verify(userService).getAddressDtoDetails(address);
        verify(commentsService).getComments("MEM-1");
    }

    @Test
    void getRegistrationTracking_shouldSetAddressNull_whenMemberHasNoAddress() {
        // Arrange
        Member member = memberWithUserAndStatus("MEM-1", 1L);
        member.setAddress(null);

        when(memberRepository.findAll()).thenReturn(List.of(member));
        when(userService.getMemberDtoDetails(member)).thenReturn(new MemberDto());
        when(userService.getRoleDtoDetails(member.getUser().getUserId())).thenReturn(new RoleDto(2L, "MEMBER"));
        when(commentsService.getComments("MEM-1")).thenReturn(Collections.emptyList());

        // Act
        List<RegistrationTrackingDTO> result = sut.getRegistrationTracking();

        // Assert
        assertEquals(1, result.size());
        assertNull(result.get(0).getAddress());

        verify(userService, never()).getAddressDtoDetails(any(Address.class));
        verify(commentsService).getComments("MEM-1");
    }

    @Test
    void reviewApplicationDecision_shouldThrow_whenMemberNotFound() {
        // Arrange
        ApplicationReviewDecisionDTO dto = new ApplicationReviewDecisionDTO("MEM-404", new RoleDto(2, "MEMBER"), new ApplicationStatusDto(1, "Submitted"));
        when(memberRepository.findById("MEM-404")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () -> sut.reviewApplicationDecision(dto));
        verify(memberRepository).findById("MEM-404");
        verifyNoMoreInteractions(memberRepository);
        verifyNoInteractions(userRoleRepository, applicationStatusRepository, userService);
    }

    @Test
    void reviewApplicationDecision_shouldExpireOldRoleAndAssignNewRole_whenSubmittedAndRoleChanged() {
        // Arrange
        Member member = memberWithUserAndStatus("MEM-1", 1L);
        member.setAddress(null); // => IN_PROGRESS

        ApplicationReviewDecisionDTO dto = new ApplicationReviewDecisionDTO(
                "MEM-1",
                new RoleDto(3L, "ADMIN"),
                new ApplicationStatusDto(AppConstantsUtil.APPL_STS_SUBMITTED, "Submitted")
        );

        UserRole existingRole = new UserRole();
        UserRolePK existingPk = new UserRolePK();
        existingPk.setUserId(member.getUser().getUserId());
        existingPk.setRoleId(AppConstantsUtil.MEMBER_ROLE_ID);
        existingRole.setId(existingPk);

        when(memberRepository.findById("MEM-1")).thenReturn(Optional.of(member));
        when(userRoleRepository.findByIdUserIdAndUserRoleExptnIsNull(member.getUser().getUserId())).thenReturn(Optional.of(existingRole));

        ApplicationStatus inProgress = new ApplicationStatus();
        inProgress.setId(AppConstantsUtil.APPL_STS_IN_PROGRESS);
        inProgress.setStatusName("In Progress");
        when(applicationStatusRepository.findById(AppConstantsUtil.APPL_STS_IN_PROGRESS)).thenReturn(Optional.of(inProgress));

        when(memberRepository.save(any(Member.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        String result = sut.reviewApplicationDecision(dto);

        // Assert
        assertEquals("Successfully reviewed application decision", result);

        verify(userRoleRepository).findByIdUserIdAndUserRoleExptnIsNull(member.getUser().getUserId());
        assertNotNull(existingRole.getUserRoleExptn(), "Old role should be expired");

        verify(userRoleRepository).save(userRoleCaptor.capture());
        assertEquals(member.getUser().getUserId(), userRoleCaptor.getValue().getId().getUserId());
        assertEquals(3L, userRoleCaptor.getValue().getId().getRoleId());

        verify(applicationStatusRepository).findById(AppConstantsUtil.APPL_STS_IN_PROGRESS);
        verify(memberRepository).save(memberCaptor.capture());
        assertSame(inProgress, memberCaptor.getValue().getApplicationStatus());

        verify(userService).logApplicationStatus(memberCaptor.getValue(), AppConstantsUtil.APPLICATION_TYPE, AppConstantsUtil.IPROGRESS_APPL_COMMENT);
    }

    @Test
    void reviewApplicationDecision_shouldNotChangeRole_whenSubmittedAndRoleIsMemberRole() {
        // Arrange
        Member member = memberWithUserAndStatus("MEM-1", 1L);
        member.setAddress(new Address()); // => READY

        ApplicationReviewDecisionDTO dto = new ApplicationReviewDecisionDTO(
                "MEM-1",
                new RoleDto(AppConstantsUtil.MEMBER_ROLE_ID, "MEMBER"),
                new ApplicationStatusDto(AppConstantsUtil.APPL_STS_SUBMITTED, "Submitted")
        );

        when(memberRepository.findById("MEM-1")).thenReturn(Optional.of(member));

        ApplicationStatus ready = new ApplicationStatus();
        ready.setId(AppConstantsUtil.APPL_STS_READY);
        ready.setStatusName("Ready");
        when(applicationStatusRepository.findById(AppConstantsUtil.APPL_STS_READY)).thenReturn(Optional.of(ready));
        when(memberRepository.save(any(Member.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        sut.reviewApplicationDecision(dto);

        // Assert
        verify(userRoleRepository, never()).findByIdUserIdAndUserRoleExptnIsNull(anyLong());
        verify(userRoleRepository, never()).save(any(UserRole.class));

        verify(memberRepository).save(memberCaptor.capture());
        assertSame(ready, memberCaptor.getValue().getApplicationStatus());

        verify(userService).logApplicationStatus(memberCaptor.getValue(), AppConstantsUtil.APPLICATION_TYPE, AppConstantsUtil.READY_APPL_COMMENT);
    }

    @Test
    void reviewApplicationDecision_shouldSetApprovedStatusAndLog_whenApproved() {
        // Arrange
        Member member = memberWithUserAndStatus("MEM-1", 1L);

        ApplicationReviewDecisionDTO dto = new ApplicationReviewDecisionDTO(
                "MEM-1",
                new RoleDto(AppConstantsUtil.MEMBER_ROLE_ID, "MEMBER"),
                new ApplicationStatusDto(AppConstantsUtil.APPL_STS_APPROVED, "Approved")
        );

        ApplicationStatus approved = new ApplicationStatus();
        approved.setId(AppConstantsUtil.APPL_STS_APPROVED);
        approved.setStatusName("Approved");

        when(memberRepository.findById("MEM-1")).thenReturn(Optional.of(member));
        when(applicationStatusRepository.findById(AppConstantsUtil.APPL_STS_APPROVED)).thenReturn(Optional.of(approved));

        // Act
        sut.reviewApplicationDecision(dto);

        // Assert
        verify(memberRepository).save(memberCaptor.capture());
        assertSame(approved, memberCaptor.getValue().getApplicationStatus());
        verify(userService).logApplicationStatus(memberCaptor.getValue(), AppConstantsUtil.APPLICATION_TYPE, AppConstantsUtil.APPROVED_APPL_COMMENT);
    }

    @Test
    void reviewApplicationDecision_shouldSetRejectedStatusAndLog_whenRejected() {
        // Arrange
        Member member = memberWithUserAndStatus("MEM-1", 1L);

        ApplicationReviewDecisionDTO dto = new ApplicationReviewDecisionDTO(
                "MEM-1",
                new RoleDto(AppConstantsUtil.MEMBER_ROLE_ID, "MEMBER"),
                new ApplicationStatusDto(AppConstantsUtil.APPL_STS_REJECTED, "Rejected")
        );

        ApplicationStatus rejected = new ApplicationStatus();
        rejected.setId(AppConstantsUtil.APPL_STS_REJECTED);
        rejected.setStatusName("Rejected");

        when(memberRepository.findById("MEM-1")).thenReturn(Optional.of(member));
        when(applicationStatusRepository.findById(AppConstantsUtil.APPL_STS_REJECTED)).thenReturn(Optional.of(rejected));

        // Act
        sut.reviewApplicationDecision(dto);

        // Assert
        verify(memberRepository).save(memberCaptor.capture());
        assertSame(rejected, memberCaptor.getValue().getApplicationStatus());
        verify(userService).logApplicationStatus(memberCaptor.getValue(), AppConstantsUtil.APPLICATION_TYPE, AppConstantsUtil.REJECTED_APPL_COMMENT);
    }

    @Test
    void getApplicationProgressHistory_shouldMapHistoryEntries() {
        // Arrange
        Member member = memberWithUserAndStatus("MEM-1", 1L);
        when(memberRepository.findById("MEM-1")).thenReturn(Optional.of(member));

        ApplicationStatus status = new ApplicationStatus();
        status.setId(1L);
        status.setStatusName("Submitted");

        ApplicationStatusHistory h1 = new ApplicationStatusHistory();
        h1.setId(10L);
        h1.setApplicationStatus(status);
        h1.setApplicationType("T");
        h1.setComment("C");

        when(applicationStatusHistoryRepository.findByMember(member)).thenReturn(List.of(h1));

        // Act
        List<ApplicationStatusHistoryDto> result = sut.getApplicationProgressHistory("MEM-1");

        // Assert
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
        assertEquals("Submitted", result.get(0).getApplicationStatus());
        assertEquals("T", result.get(0).getApplicationType());
        assertEquals("C", result.get(0).getComment());

        verify(memberRepository).findById("MEM-1");
        verify(applicationStatusHistoryRepository).findByMember(member);
    }

    @Test
    void updateUserProfile_shouldReturnMemberNotFound_whenMemberMissing() {
        // Arrange
        UpdateUserProfileDTO dto = new UpdateUserProfileDTO(new MemberDto(), new AddressDto());
        dto.getMember().setMemberId("MEM-404");
        when(memberRepository.findById("MEM-404")).thenReturn(Optional.empty());

        // Act
        String result = sut.updateUserProfile(dto);

        // Assert
        assertEquals("Member not found", result);
        verify(memberRepository).findById("MEM-404");
        verifyNoInteractions(addressRepository, userService, applicationStatusRepository);
    }

    @Test
    void updateUserProfile_shouldExpireExistingAddressAndSaveNewAddressAndSetReady_whenInProgress() {
        // Arrange
        Member member = memberWithUserAndStatus("MEM-1", AppConstantsUtil.APPL_STS_IN_PROGRESS);
        Address oldAddress = new Address();
        oldAddress.setIdAddr(1L);
        member.setAddress(oldAddress);

        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId("MEM-1");
        memberDto.setPhoneNumber("555");

        AddressDto newAddressDto = new AddressDto();
        newAddressDto.setStreet("New St");
        newAddressDto.setCity("NYC");

        UpdateUserProfileDTO dto = new UpdateUserProfileDTO(memberDto, newAddressDto);

        when(memberRepository.findById("MEM-1")).thenReturn(Optional.of(member));

        Address savedNewAddress = new Address();
        savedNewAddress.setIdAddr(2L);
        when(addressRepository.save(any(Address.class))).thenReturn(savedNewAddress);

        ApplicationStatus ready = new ApplicationStatus();
        ready.setId(AppConstantsUtil.APPL_STS_READY);
        ready.setStatusName("Ready");
        when(applicationStatusRepository.findById(AppConstantsUtil.APPL_STS_READY)).thenReturn(Optional.of(ready));

        // Act
        String result = sut.updateUserProfile(dto);

        // Assert
        assertEquals("Successfully updated user profile", result);

        verify(addressRepository, times(2)).save(addressCaptor.capture());
        List<Address> savedAddresses = addressCaptor.getAllValues();
        assertEquals(2, savedAddresses.size());

        // First save: expire old address
        assertSame(oldAddress, savedAddresses.get(0));
        assertNotNull(oldAddress.getAddrExptn(), "Old address should be expired");

        // Second save: create/save new address
        assertEquals("New St", savedAddresses.get(1).getStreet());
        assertEquals("NYC", savedAddresses.get(1).getCity());

        verify(memberRepository).save(memberCaptor.capture());
        assertSame(savedNewAddress, memberCaptor.getValue().getAddress());
        assertSame(ready, memberCaptor.getValue().getApplicationStatus());

        verify(userService).logApplicationStatus(memberCaptor.getValue(), AppConstantsUtil.APPLICATION_TYPE, AppConstantsUtil.READY_APPL_COMMENT);
    }

    @Test
    void getApplicationDetials_shouldThrowBadRequest_whenMemberIdNullOrBlank() {
        // Arrange + Act + Assert
        ResponseStatusException ex1 = assertThrows(ResponseStatusException.class, () -> sut.getApplicationDetials(null));
        assertEquals(HttpStatus.BAD_REQUEST, ex1.getStatusCode());

        ResponseStatusException ex2 = assertThrows(ResponseStatusException.class, () -> sut.getApplicationDetials("   "));
        assertEquals(HttpStatus.BAD_REQUEST, ex2.getStatusCode());

        verifyNoInteractions(userService, commentsService);
    }

    @Test
    void getApplicationDetials_shouldThrowNotFound_whenUserDetailsOrMemberMissing() {
        // Arrange
        when(userService.getUserDetails("MEM-1")).thenReturn(null);

        // Act + Assert
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> sut.getApplicationDetials("MEM-1"));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());

        verify(userService).getUserDetails("MEM-1");
        verify(commentsService).getComments("MEM-1");
    }

    @Test
    void getApplicationDetials_shouldReturnSanitizedDetailsAndComments_whenMemberExists() {
        // Arrange
        UserDetialsDto raw = new UserDetialsDto();
        raw.setUser(new UserDto());
        raw.setMember(new MemberDto());
        raw.setRole(new RoleDto(2L, "MEMBER"));
        raw.setAddress(new AddressDto());

        when(userService.getUserDetails("MEM-1")).thenReturn(raw);
        when(commentsService.getComments("MEM-1")).thenReturn(List.of(new RgstrnRqstCmntDTO()));

        // Act
        ApplicationDetialsDTO result = sut.getApplicationDetials("MEM-1");

        // Assert
        assertNotNull(result);
        assertNotNull(result.getUserDetails());
        assertNotNull(result.getComments());
        assertNotNull(result.getUserDetails().getMember());
        assertNotNull(result.getUserDetails().getRole());
        assertNotNull(result.getUserDetails().getAddress());
        assertNull(result.getUserDetails().getUser(), "User object should be omitted/sanitized");

        verify(userService).getUserDetails("MEM-1");
        verify(commentsService).getComments("MEM-1");
    }

    private static Member memberWithUserAndStatus(String memberId, long statusId) {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("u");
        user.setEmail("e@e.com");

        ApplicationStatus status = new ApplicationStatus();
        status.setId(statusId);
        status.setStatusName("S");

        Member member = new Member();
        member.setMemberId(memberId);
        member.setUser(user);
        member.setFirstName("F");
        member.setLastName("L");
        member.setEmailId("e@e.com");
        member.setApplicationStatus(status);

        user.setMember(member);
        return member;
    }
}

