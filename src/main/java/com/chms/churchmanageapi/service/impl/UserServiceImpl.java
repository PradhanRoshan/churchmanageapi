package com.chms.churchmanageapi.service.impl;

import com.chms.churchmanageapi.config.JwtUtil;
import com.chms.churchmanageapi.domain.*;
import com.chms.churchmanageapi.dto.*;
import com.chms.churchmanageapi.repository.MemberRepository;
import com.chms.churchmanageapi.repository.RoleRepository;
import com.chms.churchmanageapi.repository.UserRepository;
import com.chms.churchmanageapi.repository.UserRoleRepository;
import com.chms.churchmanageapi.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    @Transactional
    public String registerUser(SignUpDTO signUpDTO) {
        Optional<User> users = userRepository.findByUsername(signUpDTO.getUser().getUsername());
        if (users.isPresent()) {
            return "Username is already in use";
        }
        Member member = new Member();
//      Create User
        User user = saveUserInfo(signUpDTO.getUser());
//      Set all members to default role as Member
        UserRole userRole = setDefaultUserRole(user);
        member.setMemberId(generateMemberId(user));
        member.setFirstName(signUpDTO.getFirstName());
        member.setLastName(signUpDTO.getLastName());
        member.setEmailId(user.getEmail());
        member.setUser(user);
        memberRepository.save(member);
        return "User registered successfully";
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
        RoleDto roleDto = getRoleDtoDetials(user.getUserId());
        UserDto userDto = getUserDtoDetials(user);
        MemberDto memberDto = getMemberDtoDetials(user.getMember());
        if(null != user.getMember().getAddress()){
            AddressDto addressDto = getAddressDtoDetials(user.getMember().getAddress());
            detialsDto.setAddress(addressDto);
        }
        detialsDto.setRole(roleDto);
        detialsDto.setMember(memberDto);
        detialsDto.setUser(userDto);
        return detialsDto;
    }

    private RoleDto getRoleDtoDetials(long userId) {
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

    private AddressDto getAddressDtoDetials(Address address) {
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

    private MemberDto getMemberDtoDetials(Member member) {
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
        userRolePK.setRoleId(2);
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
