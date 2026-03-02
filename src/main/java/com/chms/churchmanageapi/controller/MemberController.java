package com.chms.churchmanageapi.controller;

import com.chms.churchmanageapi.dto.*;
import com.chms.churchmanageapi.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing member-related operations.
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * Retrieves the registration tracking details for all members.
     *
     * @return A list of {@link RegistrationTrackingDTO} objects.
     */
    @Operation(summary = "Get registration tracking details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved registration tracking details"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/registration-tracking", produces = "application/json")
    public ResponseEntity<List<RegistrationTrackingDTO>> getRegistrationTracking() {
        List<RegistrationTrackingDTO> trackingDetails = memberService.getRegistrationTracking();
        return ResponseEntity.ok(trackingDetails);
    }

    /**
     * Processes the application review decision.
     *
     * @param reviewDecisionDTO The review decision details.
     * @return A success message.
     */
    @Operation(summary = "Review application decision")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed the review decision"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/review-application", produces = "application/json")
    public ResponseEntity<String> reviewApplicationDecision(@RequestBody ApplicationReviewDecisionDTO reviewDecisionDTO) {
        String response = memberService.reviewApplicationDecision(reviewDecisionDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the user profile with the provided details.
     *
     * @param updateUserProfileDTO The user profile update details.
     * @return A success message.
     */
    @Operation(summary = "Update user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the user profile"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/update-user-profile", produces = "application/json")
    public ResponseEntity<String> updateUserProfile(@RequestBody UpdateUserProfileDTO updateUserProfileDTO) {
        String response = memberService.updateUserProfile(updateUserProfileDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the application progress history for a specific member.
     *
     * @param memberID The ID of the member.
     * @return A list of {@link ApplicationStatusHistoryDto} objects.
     */
    @Operation(summary = "Get application progress history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved application progress history"),
            @ApiResponse(responseCode = "400", description = "Invalid member ID"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/application-progress/{memberID}", produces = "application/json")
    public ResponseEntity<List<ApplicationStatusHistoryDto>> getAppStsHistory(@PathVariable String memberID) {
        List<ApplicationStatusHistoryDto> history = memberService.getApplicationProgressHistory(memberID);
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Get application details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved registration tracking details"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/getApplicationsDetials/{memberID}", produces = "application/json")
    public ResponseEntity<ApplicationDetialsDTO> getApplicationDetials(@PathVariable String memberID) {
        ApplicationDetialsDTO applicationDetialsDTO = memberService.getApplicationDetials(memberID);
        return ResponseEntity.ok(applicationDetialsDTO);
    }

}