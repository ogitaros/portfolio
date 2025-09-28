package com.ginotoro.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ginotoro.backend.dto.LoginUserDto;
import com.ginotoro.backend.dto.RegisterUserDto;
import com.ginotoro.backend.entity.UserM;
import com.ginotoro.backend.entity.UserPassword;
import com.ginotoro.backend.repository.UserMRepository;
import com.ginotoro.backend.repository.UserPasswordRepository;

@Service
public class AuthService {

    private final UserMRepository userMRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    UserPasswordRepository userPasswordRepository;

    public AuthService(
            UserMRepository userMRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userMRepository = userMRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public void signup(RegisterUserDto dto) {
        UserM user = new UserM();
        user.setEmail(dto.getEmail());
        user.setDisplayName(dto.getDisplayname());
        user.setStatus("1");
        user.setCreator("ginotoro.com");
        user = userMRepository.save(user);
        UserPassword pass = new UserPassword();
        pass.setUserId(user.getUserId());
        pass.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        pass.setIsActive(true);
        pass.setCreator("ginotoro.com");
        userPasswordRepository.save(pass);

    }

    public UserM authenticate(LoginUserDto input) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

        return userMRepository.findActiveByEmail(input.getEmail()).orElseThrow();
    }

    public UserM getUserInfo(String email) {
        return userMRepository.findActiveByEmail(email).orElseThrow();
    }

}
