package com.chms.churchmanageapi.controller;

import com.chms.churchmanageapi.dto.ApplicationReviewDecisionDTO;
import com.chms.churchmanageapi.dto.ApplicationStatusHistoryDto;
import com.chms.churchmanageapi.dto.RegistrationTrackingDTO;
import com.chms.churchmanageapi.dto.UpdateUserProfileDTO;
import com.chms.churchmanageapi.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRegistrationTracking_ShouldReturnListOfRegistrationTracking() {
        // Arrange
        List<RegistrationTrackingDTO> expectedTrackings = Arrays.asList(new RegistrationTrackingDTO());
        when(memberService.getRegistrationTracking()).thenReturn(expectedTrackings);

        // Act
        ResponseEntity<List<RegistrationTrackingDTO>> response = memberController.getRegistrationTracking();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedTrackings, response.getBody());
    }

    @Test
    void reviewApplicationDecision_ShouldReturnSuccessMessage() {
        // Arrange
        ApplicationReviewDecisionDTO reviewDecisionDTO = new ApplicationReviewDecisionDTO();
        String expectedResponse = "Success";
        when(memberService.reviewApplicationDecision(reviewDecisionDTO)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = memberController.reviewApplicationDecision(reviewDecisionDTO);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void updateUserProfile_ShouldReturnSuccessMessage() {
        // Arrange
        UpdateUserProfileDTO updateUserProfileDTO = new UpdateUserProfileDTO();
        String expectedResponse = "Profile updated successfully";
        when(memberService.updateUserProfile(updateUserProfileDTO)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = memberController.updateUserProfile(updateUserProfileDTO);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void getAppStsHistory_ShouldReturnApplicationStatusHistory() {
        // Arrange
        String memberID = "123";
        List<ApplicationStatusHistoryDto> expectedHistory = Arrays.asList(new ApplicationStatusHistoryDto());
        when(memberService.getApplicationProgressHistory(memberID)).thenReturn(expectedHistory);

        // Act
        ResponseEntity<List<ApplicationStatusHistoryDto>> response = memberController.getAppStsHistory(memberID);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedHistory, response.getBody());
    }
}