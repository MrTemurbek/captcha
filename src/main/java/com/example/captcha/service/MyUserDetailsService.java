package com.example.captcha.service;

import com.example.captcha.entity.Users;
import com.example.captcha.repo.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepo userRepository;
    ObjectMapper objectMapper;

    public MyUserDetailsService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (userRepository.findByUsername(username)==null  ){
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(userRepository.findByUsername(username));
    }
}
