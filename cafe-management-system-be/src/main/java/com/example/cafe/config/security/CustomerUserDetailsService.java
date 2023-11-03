package com.example.cafe.config.security;

import com.example.cafe.model.entity.UserEntity;
import com.example.cafe.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Getter
    private UserEntity userEntity;

    @Autowired
    public CustomerUserDetailsService(@NotNull final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NotNull final String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername: {}", username);
        Optional<UserEntity> userEntityWrapper = userRepository.findByEmail(username);
        if (userEntityWrapper.isPresent()) {
            userEntity = userEntityWrapper.get();
            return new User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
}