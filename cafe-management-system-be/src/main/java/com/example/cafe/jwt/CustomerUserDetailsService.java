package com.example.cafe.jwt;

import com.example.cafe.model.entity.UserEntity;
import com.example.cafe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    public CustomerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserEntity userDetail;
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        userDetail = userRepository.findByEmailId(username);
        if(!Objects.isNull(userDetail))
            return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        else throw new UsernameNotFoundException("User not found");
    }
}
