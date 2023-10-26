package com.example.cafe.service.impl;

import com.example.cafe.config.security.CustomerUserDetailsService;
import com.example.cafe.config.security.JwtFilter;
import com.example.cafe.config.security.JwtUtil;
import com.example.cafe.constants.CafeConstants;
import com.example.cafe.mapper.UserMapper;
import com.example.cafe.model.dto.UserDto;
import com.example.cafe.model.entity.UserEntity;
import com.example.cafe.repository.UserRepository;
import com.example.cafe.service.UserService;
import com.example.cafe.util.CafeUtils;
import com.example.cafe.util.EmailUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final CustomerUserDetailsService customerUserDetailsService;

    private final JwtUtil jwtUtil;

    private final JwtFilter jwtFilter;

    private final EmailUtils emailUtils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager,
                           CustomerUserDetailsService customerUserDetailsService, JwtUtil jwtUtil,
                           JwtFilter jwtFilter, EmailUtils emailUtils) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.customerUserDetailsService = customerUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
        this.emailUtils = emailUtils;
    }

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup: {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                Optional<UserEntity> userWrapper = userRepository.findByEmail(requestMap.get("email"));
                if (userWrapper.isEmpty()) {
                    userRepository.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Email already exists.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Problem in method signUp: {}", e);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login: {}", requestMap);
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()) {
                if (customerUserDetailsService.getUserEntity().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("{\"token\":\""
                            + jwtUtil.generateToken(customerUserDetailsService.getUserEntity().getEmail(),
                            customerUserDetailsService.getUserEntity().getRole()) + "\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("\"message\":\"Wait for admin approval.\"", HttpStatus.BAD_REQUEST);
                }
            }


        } catch (Exception ex) {
            log.error("{}", ex);
        }

        return new ResponseEntity<String>("\"message\":\"Bad Credentials.\"", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        try {
            if (jwtFilter.isAdmin()) {
                final List<UserEntity> users = userRepository.findAll();
                final List<UserDto> usersDto = users.stream().map(u -> UserMapper.INSTANCE.userEntityToUserDto(u)).collect(Collectors.toList());
                return new ResponseEntity<>(usersDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<List<UserDto>>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            log.error("Failed call getAllUsers", ex);
            return new ResponseEntity<List<UserDto>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<UserEntity> userEntityWrapper = userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if (userEntityWrapper.isPresent()) {
                    userRepository.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"), userEntityWrapper.get().getEmail(), userRepository.findByRole("admin"));
                    return CafeUtils.getResponseEntity("User Status Updated Successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("User id does not exist", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            log.error("Failed call update", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            Optional<UserEntity> userWrapper = userRepository.findByEmail(jwtFilter.getCurrentUser());
            if (userWrapper.isPresent()) {
                UserEntity user = userWrapper.get();
                if (user.getPassword().equals(requestMap.get("oldPassword"))) {
                    user.setPassword(requestMap.get("newPassword"));
                    userRepository.save(user);
                    return CafeUtils.getResponseEntity("Password update successfully", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Incorrect old password", HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            log.error("Failed call changePassword", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            Optional<UserEntity> userWrapper = userRepository.findByEmail(requestMap.get("email"));
            if (userWrapper.isPresent() && !Strings.isNullOrEmpty(userWrapper.get().getEmail())) {
                UserEntity user = userWrapper.get();
                emailUtils.forgotMail(user.getEmail(), "Credentials by Cafe Management System", user.getPassword());
                return CafeUtils.getResponseEntity("Check your mail for credentials", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            log.error("Failed call changePassword", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendMailToAllAdmin(String status, String user, List<UserEntity> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if (status != null && status.equalsIgnoreCase("true")) {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),
                    "Account Approve",
                    String.format("USER: %s is approved by ADMIN: %s", user, jwtFilter.getCurrentUser()),
                    allAdmin.stream().map(a -> a.getEmail()).collect(Collectors.toList()));
        } else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),
                    "Account Disable",
                    String.format("USER: %s is disabled by ADMIN: %s", user, jwtFilter.getCurrentUser()),
                    allAdmin.stream().map(a -> a.getEmail()).collect(Collectors.toList()));
        }
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        return requestMap.containsKey("name")
                && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email")
                && requestMap.containsKey("password");
    }

    private UserEntity getUserFromMap(Map<String, String> requestMap) {
        final UserEntity user = new UserEntity();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }
}