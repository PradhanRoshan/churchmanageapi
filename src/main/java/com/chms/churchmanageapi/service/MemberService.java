package com.chms.churchmanageapi.service;

import com.chms.churchmanageapi.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MemberService {
    
    List<RegistrationTrackingDTO> getRegistrationTracking();

    String reviewApplicationDecision(ApplicationReviewDecisionDTO reviewDecisionDTO);

    List<ApplicationStatusHistoryDto> getApplicationProgressHistory(String memberID);

    String updateUserProfile(UpdateUserProfileDTO updateUserProfileDTO);

    ApplicationDetialsDTO getApplicationDetials(String memberID);
}
