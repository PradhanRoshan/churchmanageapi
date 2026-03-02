package com.chms.churchmanageapi.service.impl;

import com.chms.churchmanageapi.config.AppConstantsUtil;
import com.chms.churchmanageapi.domain.*;
import com.chms.churchmanageapi.dto.*;
import com.chms.churchmanageapi.repository.*;
import com.chms.churchmanageapi.service.CommentsService;
import com.chms.churchmanageapi.service.MemberService;
import com.chms.churchmanageapi.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.chms.churchmanageapi.config.AppConstantsUtil.*;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final UserService userService;
    private final CommentsService commentsService;
    private final ApplicationStatusRepository applicationStatusRepository;
    private final UserRoleRepository userRoleRepository;
    private final ApplicationStatusHistoryRepository applicationStatusHistoryRepository;
    private final AddressRepository addressRepository;

    public MemberServiceImpl(
            MemberRepository memberRepository,
            UserService userService,
            CommentsService commentsService,
            ApplicationStatusRepository applicationStatusRepository,
            UserRoleRepository userRoleRepository,
            ApplicationStatusHistoryRepository applicationStatusHistoryRepository,
            AddressRepository addressRepository
    ) {
        this.memberRepository = memberRepository;
        this.userService = userService;
        this.commentsService = commentsService;
        this.applicationStatusRepository = applicationStatusRepository;
        this.userRoleRepository = userRoleRepository;
        this.applicationStatusHistoryRepository = applicationStatusHistoryRepository;
        this.addressRepository = addressRepository;
    }

    private static final Integer MEMBER_ROLE_ID = 2;
    private static final long ID_APPL_STS_SUBMITTED = 1;
    private static final long ID_APPL_STS_APPROVED = 4;
    private static final long ID_APPL_STS_REJECTED = 5;



    /**
     * Builds a registration tracking view for all members.
     * <p>
     * For each {@link Member} returned by {@link MemberRepository#findAll()}, this method constructs a
     * {@link RegistrationTrackingDTO} by:
     * <ul>
     *   <li>Mapping the member entity to a {@code MemberDto} via {@link UserService#getMemberDtoDetails(Member)}.</li>
     *   <li>Resolving role details via {@link UserService} using the member's user id.</li>
     *   <li>Mapping the current {@link ApplicationStatus} to an {@link ApplicationStatusDto}.
     *       If the status is {@code null}, the DTO status will be {@code null}.</li>
     *   <li>Mapping the member's {@link Address} to an {@link AddressDto} when present; otherwise {@code null}.</li>
     *   <li>Fetching the member's registration request comments via {@link CommentsService#getComments(String)}.</li>
     * </ul>
     *
     * @return a list of registration tracking DTOs; never {@code null} (maybe empty when no members exist)
     *
     * @throws RuntimeException if underlying dependencies throw (for example, if a member has an unexpected
     *                          null {@code user} or {@code userId})
     */
    @Override
    public List<RegistrationTrackingDTO> getRegistrationTracking() {
        return memberRepository.findAll().stream()
                .map(member -> {
                    RegistrationTrackingDTO registrationTrackingDTO = new RegistrationTrackingDTO();
                    registrationTrackingDTO.setUserMember(userService.getMemberDtoDetails(member));
                    registrationTrackingDTO.setRole(userService.getRoleDtoDetails(member.getUser().getUserId()));
                    registrationTrackingDTO.setApplicationStatus(getApplicationStatusDetials(member.getApplicationStatus()));
                    registrationTrackingDTO.setAddress((member.getAddress() != null) ? userService.getAddressDtoDetails(member.getAddress()) : null);
                    registrationTrackingDTO.setComments(commentsService.getComments(member.getMemberId()));
                    return registrationTrackingDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public String reviewApplicationDecision(ApplicationReviewDecisionDTO reviewDecisionDTO) {
        Member memberDetails = memberRepository.findById(reviewDecisionDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

//        Handling Submitted application Status
        if (ID_APPL_STS_SUBMITTED == reviewDecisionDTO.getApplicationStatus().getStatusId()) {

//            Optional<UserRole> userRole = userRoleRepository.findById_UserIdAndId_RoleIdAndUserRoleExptnIsNull(memberDetails.getUser().getUserId(), reviewDecisionDTO.getRole().getRoleId());
//            if (userRole.isEmpty()) {
            if (MEMBER_ROLE_ID != reviewDecisionDTO.getRole().getRoleId()) {
                UserRole userRoleDetails = userRoleRepository.findByIdUserIdAndUserRoleExptnIsNull(memberDetails.getUser().getUserId()).get();
                userRoleDetails.setUserRoleExptn(new Date());
                UserRolePK userRolePK = new UserRolePK();
                userRolePK.setUserId(memberDetails.getUser().getUserId());
                userRolePK.setRoleId(reviewDecisionDTO.getRole().getRoleId());
                UserRole userRoles = new UserRole();
                userRoles.setId(userRolePK);
                userRoleRepository.save(userRoles);
            }
            // Update Application Status
            ApplicationStatus applicationStatus = memberDetails.getAddress() !=null? getApplicationStatusById(APPL_STS_READY):getApplicationStatusById(APPL_STS_IN_PROGRESS);
            memberDetails.setApplicationStatus(applicationStatus);
            memberRepository.save(memberDetails);

            // ✅ Log Application Status
            userService.logApplicationStatus(memberDetails, APPLICATION_TYPE, "Ready".equals(applicationStatus.getStatusName())?READY_APPL_COMMENT:IPROGRESS_APPL_COMMENT);
        }
        // Handling Approved application Status
        else if (ID_APPL_STS_APPROVED == reviewDecisionDTO.getApplicationStatus().getStatusId()) {
            ApplicationStatus applicationStatus = getApplicationStatusById(ID_APPL_STS_APPROVED);
            memberDetails.setApplicationStatus(applicationStatus);
            memberRepository.save(memberDetails);
            // ✅ Log Application Status
            userService.logApplicationStatus(memberDetails, APPLICATION_TYPE, APPROVED_APPL_COMMENT);
        }
        // Handling Rejected application Status
        else if (ID_APPL_STS_REJECTED == reviewDecisionDTO.getApplicationStatus().getStatusId()) {
            ApplicationStatus applicationStatus = getApplicationStatusById(ID_APPL_STS_REJECTED);
            memberDetails.setApplicationStatus(applicationStatus);
//            memberDetails.getUser().setUserExptn(new Date());
            memberDetails.setMemberExptn(new Date());
            memberRepository.save(memberDetails);
            // ✅ Log Application Status
            userService.logApplicationStatus(memberDetails, APPLICATION_TYPE, REJECTED_APPL_COMMENT);
        }
        return "Successfully reviewed application decision";
    }

    private ApplicationStatus getApplicationStatusById(long idApplSts) {
        return applicationStatusRepository
                .findById(idApplSts)
                .orElseThrow(() -> new RuntimeException("Application status not found"));
    }

    @Transactional
    @Override
    public List<ApplicationStatusHistoryDto> getApplicationProgressHistory(String memberID) {
        Member member = memberRepository.findById(memberID).orElseThrow(() -> new RuntimeException("Member not found"));

       List<ApplicationStatusHistory> applicationStatusHistoryList = applicationStatusHistoryRepository.findByMember(member);
       List<ApplicationStatusHistoryDto> applicationStatusHistoryDtoList = new ArrayList<>();
       for (ApplicationStatusHistory history : applicationStatusHistoryList) {
           ApplicationStatusHistoryDto statusHistoryDto = new ApplicationStatusHistoryDto();
           statusHistoryDto.setId(history.getId());
            statusHistoryDto.setApplicationStatus(history.getApplicationStatus().getStatusName());
            statusHistoryDto.setApplicationType(history.getApplicationType());
            statusHistoryDto.setComment(history.getComment());
            statusHistoryDto.setDttmCreate(history.getDttmCreate());
            statusHistoryDto.setDttmLstUpdt(history.getDttmLstUpdt());
            statusHistoryDto.setIdUserCreate(history.getIdUserCreate());
            statusHistoryDto.setIdUserLstUpdt(history.getIdUserLstUpdt());
            applicationStatusHistoryDtoList.add(statusHistoryDto);
       }
       return applicationStatusHistoryDtoList;
    }

    @Override
    public String updateUserProfile(UpdateUserProfileDTO updateUserProfileDTO) {
    Optional<Member> member = memberRepository.findById(updateUserProfileDTO.getMember().getMemberId());
    if (member.isEmpty()) {
        return "Member not found";
    }
        Member memberDetails = member.get();
//        memberDetails.getAddress() != null?
        if(null != memberDetails.getAddress()){
            memberDetails.getAddress().setAddrExptn(new Date());
            addressRepository.save(memberDetails.getAddress());
        }
        Address address = saveAddress(updateUserProfileDTO.getAddress());
        System.out.println(address.getIdAddr());
        memberDetails.setAddress(address);
        memberDetails.setMemberDob(updateUserProfileDTO.getMember().getMemberDob());
        memberDetails.setMaritalStatus(updateUserProfileDTO.getMember().getMaritalStatus());
        memberDetails.setGender(updateUserProfileDTO.getMember().getGender());
        memberDetails.setMiddleName(updateUserProfileDTO.getMember().getMiddleName());
        memberDetails.setPhone(updateUserProfileDTO.getMember().getPhoneNumber());
 // ✅ Log Application Status
        if(memberDetails.getApplicationStatus().getId() == APPL_STS_IN_PROGRESS){
            ApplicationStatus applicationStatus = getApplicationStatusById(APPL_STS_READY);
            memberDetails.setApplicationStatus(applicationStatus);
            userService.logApplicationStatus(memberDetails, AppConstantsUtil.APPLICATION_TYPE, AppConstantsUtil.READY_APPL_COMMENT);
        }
        memberRepository.save(memberDetails);
        return "Successfully updated user profile";
    }


    /**
     * Returns application details for a given member id.
     * <p>
     * This method:
     * <ul>
     *   <li>Validates that {@code memberID} is not {@code null} and not blank.</li>
     *   <li>Fetches user/member details via {@link UserService#getUserDetails(String)}.</li>
     *   <li>Fetches registration request comments via {@link CommentsService#getComments(String)}.</li>
     *   <li>Creates a sanitized {@link UserDetialsDto} copy containing only member, role, and address
     *       (to avoid leaking internal objects/credentials if present).</li>
     * </ul>
     *
     * @param memberID the member identifier used to fetch application details (must not be {@code null} or blank)
     * @return the aggregated application details, including a sanitized user details DTO and associated comments
     *
     * @throws ResponseStatusException with {@link HttpStatus#BAD_REQUEST} when {@code memberID} is {@code null} or blank
     * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} when no member details are found for the given id
     */
    @Override
    public ApplicationDetialsDTO getApplicationDetials(String memberID) {
        // Validate input
        if (memberID == null || memberID.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "memberID must be provided");
        }
        UserDetialsDto userDetialsDto = userService.getUserDetails(memberID);
        List<RgstrnRqstCmntDTO> rgstrnRqstCmnts = commentsService.getComments(memberID);

        if (userDetialsDto == null || userDetialsDto.getMember() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found for id: " + memberID);
        }

        // Create a sanitized copy of UserDetialsDto to avoid leaking credentials or internal user object
        UserDetialsDto detialsDto = new UserDetialsDto();
        detialsDto.setMember(userDetialsDto.getMember());
        detialsDto.setRole(userDetialsDto.getRole());
        detialsDto.setAddress(userDetialsDto.getAddress());
        return new ApplicationDetialsDTO(detialsDto, rgstrnRqstCmnts);
    }

    private Address saveAddress(AddressDto address) {
        Address addressDetails = new Address();
        addressDetails.setCity(address.getCity());
        addressDetails.setState(address.getState());
        addressDetails.setAptNo(address.getAptNo());
        addressDetails.setStreet(address.getStreet());
        addressDetails.setZip(address.getZip());
        return  addressRepository.save(addressDetails);
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
