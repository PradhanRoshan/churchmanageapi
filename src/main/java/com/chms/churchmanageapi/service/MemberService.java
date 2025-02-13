package com.chms.churchmanageapi.service;

import com.chms.churchmanageapi.dto.ApplicationReviewDecisionDTO;
import com.chms.churchmanageapi.dto.RegistrationTrackingDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MemberService {
    
    List<RegistrationTrackingDTO> getRegistrationTracking();

    String reviewApplicationDecision(ApplicationReviewDecisionDTO reviewDecisionDTO);
}
