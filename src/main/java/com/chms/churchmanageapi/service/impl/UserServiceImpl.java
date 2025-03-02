package com.chms.churchmanageapi.service.impl;
import com.chms.churchmanageapi.config.AppConstantsUtil;
import com.chms.churchmanageapi.config.JwtUtil;
import com.chms.churchmanageapi.config.UserContextUtil;
import com.chms.churchmanageapi.domain.*;
import com.chms.churchmanageapi.dto.*;
import com.chms.churchmanageapi.repository.*;
import com.chms.churchmanageapi.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ApplicationStatusRepository applicationStatusRepository;

    @Autowired
    private ApplicationStatusHistoryRepository applicationStatusHistoryRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

//    private static final int MEMBER_ROLE_ID = 2;
//    private static final long APPL_STS_SUBMITTED = 1L;
//    private static final String APPLICATION_TYPE = "New Registration";
//    private static final String APPLICATION_COMMENT = "New Registration";

    @Override
    @Transactional
    public String registerUser(SignUpDTO signUpDTO) {
        if (userRepository.findByUsername(signUpDTO.getUser().getUsername()).isPresent()) {
            return "Username is already in use";
        }
        try {
            UserContextUtil.setUser(signUpDTO.getUser().getUsername());
            // ✅ Create User & Save
            User user = saveUserInfo(signUpDTO.getUser());
            // ✅ Assign Default Role
            setDefaultUserRole(user);
            // ✅ Save Member
            Member member = createMember(signUpDTO, user);
            memberRepository.save(member);
            // ✅ Log Application Status
            logApplicationStatus(member, AppConstantsUtil.APPLICATION_TYPE, AppConstantsUtil.APPLICATION_COMMENT);
            return "User registered successfully";
        } finally {
            UserContextUtil.clear();
        }
    }

    private Member createMember(SignUpDTO signUpDTO, User user) {
        Member member = new Member();
        member.setMemberId(generateMemberId(user));
        member.setFirstName(signUpDTO.getFirstName());
        member.setApplicationStatus(applicationStatusRepository.findById(AppConstantsUtil.APPL_STS_SUBMITTED).get());
        member.setLastName(signUpDTO.getLastName());
        member.setEmailId(user.getEmail());
        member.setUser(user);
        return member;
    }

    public void logApplicationStatus(Member member, String applicationType, String comment) {
        ApplicationStatusHistory history = new ApplicationStatusHistory();
        history.setApplicationStatus(applicationStatusRepository.findById(member.getApplicationStatus().getId()).orElseThrow());
        history.setMember(member);
        history.setApplicationType(applicationType);
        history.setComment(comment);
        applicationStatusHistoryRepository.save(history);
    }

    @Override
    public UserDetialsDto getUserDetails(String memberID) {
        Optional<Member> member = memberRepository.findById(memberID);

        UserDetialsDto userDetialsDto = null;
        if (member.isPresent()) {
            userDetialsDto= new UserDetialsDto();
            Member memberDetails = member.get();
            userDetialsDto.setUser(getUserDtoDetials(memberDetails.getUser()));
            userDetialsDto.setMember(getMemberDtoDetails(memberDetails));
            if (memberDetails.getAddress() != null) {
                userDetialsDto.setAddress(getAddressDtoDetails(memberDetails.getAddress()));
            }
            userDetialsDto.setRole(getRoleDtoDetails(memberDetails.getUser().getUserId()));

            return userDetialsDto;
        }

        return userDetialsDto;
    }


    @Override
    @PreAuthorize("isAuthenticated()")
    public String resetUserPassword(ResetPasswordDTO resetPasswordDTO) {
        return userRepository.findByUsername(resetPasswordDTO.getUsername())
                .map(user -> {
                    try {
                        UserContextUtil.setUser(resetPasswordDTO.getUsername());
                        if (resetPasswordDTO.getCurrentPassword() != null &&
                                !passwordEncoder.matches(resetPasswordDTO.getCurrentPassword(), user.getPassword())) {
                            return "Incorrect current password";
                        }

                        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
                        userRepository.save(user);
                        return "Password changed successfully";
                    } finally {
                        UserContextUtil.clear();
                    }
                }).orElse("User not found");
    }


    @Override
    public ResponseEntity<LoginResponseDTO> userLoginAuthentication(AuthRequestDTO authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        UserDetialsDto userDetialsDto = getMemberInfo(authRequest);
        LoginResponseDTO loginResponse = new LoginResponseDTO();//.setToken(jwt).setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setToken(jwt);
        loginResponse.setUserRole(userRepository.findByUsername(userDetails.getUsername()).get().getRoles().get(0).getRoleId());
        loginResponse.setExpiresIn(jwtUtil.getExpirationTime());
        loginResponse.setUserDetialsDto(userDetialsDto);
        return ResponseEntity.ok(loginResponse);
    }


    private UserDetialsDto getMemberInfo(AuthRequestDTO authRequest) {
        UserDetialsDto detailsDto = new UserDetialsDto();
        User user = userRepository.findByUsername(authRequest.getUsername()).get();
        detailsDto.setRole(getRoleDtoDetails(user.getUserId()));
        detailsDto.setMember(getMemberDtoDetails(user.getMember()));
        detailsDto.setUser(getUserDtoDetials(user));
        if (user.getMember().getAddress() != null) {
            detailsDto.setAddress(getAddressDtoDetails(user.getMember().getAddress()));
        }
        return detailsDto;
    }

//    public RoleDto getRoleDtoDetails(long userId) {
//        RoleDto roleDto = new RoleDto();
//        Optional<UserRole> userRole = userRoleRepository.findByIdUserIdAndUserRoleExptnIsNull(userId);
//        Role role = userRole.get().getRole();
//        roleDto.setRoleId(role.getRoleId());
//        roleDto.setRoleName(role.getRoleName());
//        return roleDto;
//    }
    public RoleDto getRoleDtoDetails(long userId) {
        return (userRoleRepository.findByIdUserIdAndUserRoleExptnIsNull(userId))
                .map(userRole -> new RoleDto(userRole.getRole().getRoleId(), userRole.getRole().getRoleName()))
                .orElse(null);
    }

    public AddressDto getAddressDtoDetails(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setIdAddr(address.getIdAddr());
        addressDto.setStreet(address.getStreet());
        addressDto.setAptNo(address.getAptNo());
        addressDto.setAddrExptn(address.getAddrExptn());
        addressDto.setCity(address.getCity());
        addressDto.setState(address.getState());
        addressDto.setZip(address.getZip());
        return addressDto;
    }

    public MemberDto getMemberDtoDetails(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId(member.getMemberId());
        memberDto.setFirstName(member.getFirstName());
        memberDto.setLastName(member.getLastName());
        memberDto.setEmailId(member.getEmailId());
        memberDto.setMemberDob(member.getMemberDob());
        memberDto.setMiddleName(member.getMiddleName());
        memberDto.setGender(member.getGender());
        memberDto.setPhoneNumber(member.getPhone());
        memberDto.setMaritalStatus(member.getMaritalStatus());
        memberDto.setStatus(member.getMemberExptn()==null?"Active":"Inactive");
        memberDto.setApplicationSts(member.getApplicationStatus().getStatusName());
        memberDto.setDttmCreate(member.getDttmCreate());
        return memberDto;
    }

    private UserDto getUserDtoDetials(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setStatus(user.getUserExptn() == null? "Active": "Inactive");
        return userDto;
    }


    //    GetUserDetialsBy username
    private UserDto getUserDetailsByUserName(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        UserDto userDto = new UserDto();
        if(user.isPresent()) {
            User userDetails = user.get();
            userDto.setUserId(userDetails.getUserId());
            userDto.setUsername(userDetails.getUsername());
            userDto.setEmail(userDetails.getEmail());
            userDto.setStatus(userDetails.getUserExptn() == null? "Active": "Inactive");
        }
        return userDto;
    }

    private void setDefaultUserRole(User user) {
        UserRole userRole = new UserRole();
        UserRolePK userRolePK = new UserRolePK();
        userRolePK.setUserId(user.getUserId());
        userRolePK.setRoleId(AppConstantsUtil.MEMBER_ROLE_ID);
        userRole.setId(userRolePK);
        userRoleRepository.save(userRole);
    }

    private String generateMemberId(User user) {
        return "MEM-" + user.getUserId();
    }

    private User saveUserInfo(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        return userRepository.save(user);
    }
}


/**
package com.chms.churchmanageapi.service.impl;
import com.chms.churchmanageapi.config.JwtUtil;
import com.chms.churchmanageapi.config.UserContextUtil;
import com.chms.churchmanageapi.domain.*;
import com.chms.churchmanageapi.dto.*;
import com.chms.churchmanageapi.repository.*;
import com.chms.churchmanageapi.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ApplicationStatusRepository applicationStatusRepository;

    @Autowired
    private ApplicationStatusHistoryRepository applicationStatusHistoryRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final int MEMBER_ROLE_ID = 2;

    @Override
    @Transactional
    public String registerUser(SignUpDTO signUpDTO) {
        Optional<User> users = userRepository.findByUsername(signUpDTO.getUser().getUsername());
        if (users.isPresent()) {
            return "Username is already in use";
        }
        try {
            // Setting username to be used in auditing when authentication is missing
            UserContextUtil.setUser(signUpDTO.getUser().getUsername());

            Member member = new Member();
            // Create User
            User user = saveUserInfo(signUpDTO.getUser());
            // Set all members to default role as Member
            UserRole userRole = setDefaultUserRole(user);
            member.setMemberId(generateMemberId(user));
            member.setFirstName(signUpDTO.getFirstName());
            member.setApplicationStatus(applicationStatusRepository.findById(1L).get());
            member.setLastName(signUpDTO.getLastName());
            member.setEmailId(user.getEmail());
            member.setUser(user);
            memberRepository.save(member);

            // Save entry to applicationStatusHistory
            ApplicationStatusHistory applicationStatusHistory = new ApplicationStatusHistory();
            applicationStatusHistory.setApplicationStatus(applicationStatusRepository.findById(1L).get());
            applicationStatusHistory.setMember(member);
            applicationStatusHistory.setApplicationType("New Registration");
            applicationStatusHistory.setComment("New Member application submitted");
            applicationStatusHistoryRepository.save(applicationStatusHistory);

            return "User registered successfully";
        } finally {
            // Ensure the context is cleared to prevent memory leaks
            UserContextUtil.clear();
        }
    }


    @Override
    @PreAuthorize("isAuthenticated()")
    public String resetUserPassword(ResetPasswordDTO resetPasswordDTO) {
        Optional<User> user = userRepository.findByUsername(resetPasswordDTO.getUsername());
        if (user.isEmpty()) {
            return "User not found";
        }
        try {
            // Set username for auditing when authentication is missing
            UserContextUtil.setUser(resetPasswordDTO.getUsername());
            User activeUser = user.get();
            // Check if currentPassword is provided and validate it
            if (resetPasswordDTO.getCurrentPassword() != null) {
                if (!passwordEncoder.matches(resetPasswordDTO.getCurrentPassword(), activeUser.getPassword())) {
                    return "Incorrect current password";
                }
            }
            // Encode and update password
            activeUser.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
            userRepository.save(activeUser);
            return "Password changed successfully";
        } finally {
            // Ensure UserContextUtil is cleared to prevent memory leaks
            UserContextUtil.clear();
        }
    }


    @Override
    public ResponseEntity<LoginResponseDTO> userLoginAuthentication(AuthRequestDTO authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        UserDetialsDto userDetialsDto = getMemberInfo(authRequest);
        LoginResponseDTO loginResponse = new LoginResponseDTO();//.setToken(jwt).setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setToken(jwt);
        loginResponse.setUserRole(userRepository.findByUsername(userDetails.getUsername()).get().getRoles().get(0).getRoleId());
        loginResponse.setExpiresIn(jwtUtil.getExpirationTime());
        loginResponse.setUserDetialsDto(userDetialsDto);
        return ResponseEntity.ok(loginResponse);
    }

    private UserDetialsDto getMemberInfo(AuthRequestDTO authRequest) {
        UserDetialsDto detialsDto = new UserDetialsDto();
        User user = userRepository.findByUsername(authRequest.getUsername()).get();
        RoleDto roleDto = getRoleDtoDetails(user.getUserId());
        UserDto userDto = getUserDtoDetials(user);
        MemberDto memberDto = getMemberDtoDetails(user.getMember());
        if(null != user.getMember().getAddress()){
            AddressDto addressDto = getAddressDtoDetails(user.getMember().getAddress());
            detialsDto.setAddress(addressDto);
        }
        detialsDto.setRole(roleDto);
        detialsDto.setMember(memberDto);
        detialsDto.setUser(userDto);
        return detialsDto;
    }

    public RoleDto getRoleDtoDetails(long userId) {
        RoleDto roleDto = new RoleDto();
        UserRole userRole = userRoleRepository.findById_UserIdAndUserRoleExptnIsNull(userId);
        Role role = userRole.getRole();
        roleDto.setRoleId(role.getRoleId());
        roleDto.setRoleName(role.getRoleName());
        return roleDto;
    }

//    private RoleDto getRoleDtoDetials(List<Role> roles) {
//        RoleDto roleDto = new RoleDto();
//        Optional<Role> activeRole = roles.stream()
//                .filter(role -> role.   getRoleExptn() == null)
//                .findFirst();
//        if(activeRole.isPresent()){
//            Role role = activeRole.get();
//            roleDto.setRoleId(role.getRoleId());
//            roleDto.setRoleName(role.getRoleName());
//        }
//        return roleDto;
//    }

    public AddressDto getAddressDtoDetails(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setIdAddr(address.getIdAddr());
        addressDto.setStreet(address.getStreet());
        addressDto.setAptNo(address.getAptNo());
        addressDto.setAddrExptn(address.getAddrExptn());
        addressDto.setCity(address.getCity());
        addressDto.setState(address.getState());
        addressDto.setZip(address.getZip());
        return addressDto;
    }

    public MemberDto getMemberDtoDetails(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId(member.getMemberId());
        memberDto.setFirstName(member.getFirstName());
        memberDto.setLastName(member.getLastName());
        memberDto.setEmailId(member.getEmailId());
        memberDto.setMemberDob(member.getMemberDob());
        memberDto.setMiddleName(member.getMiddleName());
        memberDto.setGender(member.getGender());
        memberDto.setPhone(member.getPhone());
        memberDto.setMaritalStatus(member.getMaritalStatus());
        memberDto.setStatus(member.getMemberExptn()==null?"Active":"Inactive");
        memberDto.setDttmCreate(member.getDttmCreate());
        return memberDto;
    }

    private UserDto getUserDtoDetials(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setStatus(user.getUserExptn() == null? "Active": "Inactive");
        return userDto;
    }


//    GetUserDetialsBy username
    private UserDto getUserDetialsByUserName(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        UserDto userDto = new UserDto();
        if(user.isPresent()) {
            User userDetails = user.get();
            userDto.setUserId(userDetails.getUserId());
            userDto.setUsername(userDetails.getUsername());
            userDto.setEmail(userDetails.getEmail());
            userDto.setStatus(userDetails.getUserExptn() == null? "Active": "Inactive");
        }
        return userDto;
    }

    private UserRole setDefaultUserRole(User user) {
        UserRole userRole = new UserRole();
        UserRolePK userRolePK = new UserRolePK();
        userRolePK.setUserId(user.getUserId());
        userRolePK.setRoleId(MEMBER_ROLE_ID);
        userRole.setId(userRolePK);
        return userRoleRepository.save(userRole);
    }

    private String generateMemberId(User user) {
       String memberId = "MEM-"+user.getUserId();
        return memberId;
    }

    private User saveUserInfo(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        return userRepository.save(user);
    }
}
**/