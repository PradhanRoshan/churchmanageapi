package com.chms.churchmanageapi.service.impl;

import com.chms.churchmanageapi.domain.Member;
import com.chms.churchmanageapi.domain.User;
import com.chms.churchmanageapi.domain.UserRole;
import com.chms.churchmanageapi.domain.UserRolePK;
import com.chms.churchmanageapi.dto.SignUpDTO;
import com.chms.churchmanageapi.repository.MemberRepository;
import com.chms.churchmanageapi.repository.RoleRepository;
import com.chms.churchmanageapi.repository.UserRepository;
import com.chms.churchmanageapi.repository.UserRoleRepository;
import com.chms.churchmanageapi.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
