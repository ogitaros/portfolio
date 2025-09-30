package com.ginotoro.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ginotoro.backend.entity.UserM;
import com.ginotoro.backend.entity.UserPassword;
import com.ginotoro.backend.repository.UserMRepository;
import com.ginotoro.backend.repository.UserPasswordRepository;
import com.ginotoro.backend.security.UserDetailsImpl;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMRepository userMRepository;

    @Autowired
    private UserPasswordRepository userPasswordRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserM user = userMRepository
                .findActiveByEmail(email)
                .orElseThrow(() -> new UnsupportedOperationException("User not found with email: " + email));
        UserPassword userPassword = userPasswordRepository
                .findFirstByUserIdAndIsActiveTrueOrderByCreatedAtDesc(user.getUserId())
                .orElseThrow(() -> new UnsupportedOperationException("User not found with email(passcheck): " + email));
        return new UserDetailsImpl(user, userPassword);

    }

}
