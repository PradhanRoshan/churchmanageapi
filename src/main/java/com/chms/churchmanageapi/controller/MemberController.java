package com.chms.churchmanageapi.controller;

import com.chms.churchmanageapi.dto.ApplicationReviewDecisionDTO;
import com.chms.churchmanageapi.dto.ApplicationStatusHistoryDto;
import com.chms.churchmanageapi.dto.RegistrationTrackingDTO;
import com.chms.churchmanageapi.dto.UpdateUserProfileDTO;
import com.chms.churchmanageapi.service.MemberService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "404", description = "Payment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
})
@RestController
@RequestMapping("/member")
public class MemberController {
    
    @Autowired
    private MemberService memberService;

    @GetMapping(value = "/registration-tracking", produces = "application/json")
    public List<RegistrationTrackingDTO> getRegistrationTracking() {
        return  memberService.getRegistrationTracking();
    }

    @PostMapping(value = "/review-application", produces = "application/json")
    public String reviewApplicationDecision(@RequestBody ApplicationReviewDecisionDTO reviewDecisionDTO){
        return  memberService.reviewApplicationDecision(reviewDecisionDTO);
    }

    @PostMapping(value = "/update-user-profile", produces = "application/json")
    public String updateUserProfile(@RequestBody UpdateUserProfileDTO updateUserProfileDTO){
        return  memberService.updateUserProfile(updateUserProfileDTO);
    }

    @GetMapping(value = "/application-progress/{memberID}", produces = "application/json")
    public List<ApplicationStatusHistoryDto> getAppStsHistory(@PathVariable String memberID){
        return  memberService.getApplicationProgressHistory(memberID);
    }
}
