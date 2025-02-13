package com.chms.churchmanageapi.service.impl;

import com.chms.churchmanageapi.config.AppConstantsUtil;
import com.chms.churchmanageapi.domain.*;
import com.chms.churchmanageapi.dto.*;
import com.chms.churchmanageapi.repository.ApplicationStatusRepository;
import com.chms.churchmanageapi.repository.MemberRepository;
import com.chms.churchmanageapi.repository.UserRoleRepository;
import com.chms.churchmanageapi.service.MemberService;
import com.chms.churchmanageapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {
    
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationStatusRepository applicationStatusRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

//    private static final String APPLICATION_TYPE = "New Registration";
//    private static final String APPLICATION_COMMENT = "New Registration";


    @Override
    public List<RegistrationTrackingDTO> getRegistrationTracking() {
        return memberRepository.findAll().stream()
                .map(member -> {
                    RegistrationTrackingDTO registrationTrackingDTO = new RegistrationTrackingDTO();
                    registrationTrackingDTO.setUserMember(userService.getMemberDtoDetails(member));
                    registrationTrackingDTO.setRole(userService.getRoleDtoDetails(member.getUser().getUserId()));
                    registrationTrackingDTO.setApplicationStatus(getApplicationStatusDetials(member.getApplicationStatus()));
                    registrationTrackingDTO.setAddress((member.getAddress() != null) ? userService.getAddressDtoDetails(member.getAddress()) : null);
                    return registrationTrackingDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public String reviewApplicationDecision(ApplicationReviewDecisionDTO reviewDecisionDTO) {
        Member memberDetails = memberRepository.findById(reviewDecisionDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (reviewDecisionDTO.getApplicationStatus().getStatusId() == 2) {

            Optional<UserRole> userRole = userRoleRepository.findByIdUserIdAndUserRoleExptnIsNull(memberDetails.getUser().getUserId());
            if (userRole.isPresent()) {
                UserRole userRoleDetails = userRole.get();
                userRoleDetails.setUserRoleExptn(new Date());
                userRoleRepository.save(userRoleDetails);
            }
            UserRolePK userRolePK = new UserRolePK();
            userRolePK.setUserId(memberDetails.getUser().getUserId());
            userRolePK.setRoleId(reviewDecisionDTO.getRole().getRoleId());
            UserRole userRoles = new UserRole();
            userRoles.setId(userRolePK);
            userRoleRepository.save(userRoles);

            // Update Application Status
            ApplicationStatus applicationStatus = applicationStatusRepository
                    .findById(reviewDecisionDTO.getApplicationStatus().getStatusId())
                    .orElseThrow(() -> new RuntimeException("Application status not found"));

            memberDetails.setApplicationStatus(applicationStatus);
            memberRepository.save(memberDetails);

            // ✅ Log Application Status
            userService.logApplicationStatus(memberDetails, AppConstantsUtil.APPLICATION_TYPE, AppConstantsUtil.APPLICATION_COMMENT);
        }
        return "Successfully reviewed application decision";
    }



//    @Override
//    public String reviewApplicationDecision(ApplicationReviewDecisionDTO reviewDecisionDTO) {
//        Optional<Member> member = memberRepository.findById(reviewDecisionDTO.getMemberId());
//        if (member.isEmpty()) {
//            return "Member not found";
//        }
//        Member memberDetails = member.get();
//        if (reviewDecisionDTO.getApplicationStatus().getStatusId()==2){
//           UserRole userRole = userRoleRepository.findById_UserIdAndUserRoleExptnIsNull(memberDetails.getUser().getUserId());
//           if (userRole.getRole().getRoleId()!=reviewDecisionDTO.getRole().getRoleId()){
//               UserRolePK userRolePK = new UserRolePK();
//               userRolePK.setUserId(memberDetails.getUser().getUserId());
//               userRolePK.setRoleId(reviewDecisionDTO.getRole().getRoleId());
//               userRole.setId(userRolePK);
//               userRoleRepository.save(userRole);
//           }
//            memberDetails.setApplicationStatus(applicationStatusRepository.findById(reviewDecisionDTO.getApplicationStatus().getStatusId()).get());
//            memberRepository.save(memberDetails);
//
//            // ✅ Log Application Status
//            userService.logApplicationStatus(memberDetails, "New Registration", "New Member application In Progress");
//        }
//        return "Successfully reviewed application decision";
//    }


    //My Code-------------
//    @Override
//    public List<RegistrationTrackingDTO> getRegistrationTracking() {
//        List<Member> members = memberRepository.findAll();
//        List<RegistrationTrackingDTO> registrationTrackingDTOs = new ArrayList<>();
//            for (Member member : members) {
//                RegistrationTrackingDTO registrationTrackingDTO = new RegistrationTrackingDTO();
//                MemberDto memberDto = userService.getMemberDtoDetails(member);
//                RoleDto roleDto = userService.getRoleDtoDetails(member.getUser().getUserId());
//                ApplicationStatusDto applicationStatusDto= getApplicationStatusDetials(member.getApplicationStatus());
//                AddressDto addressDto = (member.getAddress() != null)? userService.getAddressDtoDetails(member.getAddress()): null;
//
//                registrationTrackingDTO.setUserMember(memberDto);
//                registrationTrackingDTO.setRole(roleDto);
//                registrationTrackingDTO.setAddress(addressDto);
//                registrationTrackingDTO.setApplicationStatus(applicationStatusDto);
//                registrationTrackingDTOs.add(registrationTrackingDTO);
//            }
//        return registrationTrackingDTOs;
//    }

    private ApplicationStatusDto getApplicationStatusDetials(ApplicationStatus applicationStatus) {
        if (applicationStatus == null) {
            return null;
        }
        return new ApplicationStatusDto(
                applicationStatus.getId(),
                applicationStatus.getStatusName()
        );
    }
}
