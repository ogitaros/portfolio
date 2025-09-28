package com.ginotoro.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ginotoro.backend.entity.UserM;
import com.ginotoro.backend.entity.UserPassword;
import com.ginotoro.backend.repository.UserMRepository;
import com.ginotoro.backend.repository.UserPasswordRepository;
import com.ginotoro.backend.security.UserDetailsImpl;

@Configuration
public class AuthConfig {

    @Autowired
    private UserMRepository userMRepository;
    @Autowired
    private UserPasswordRepository userPasswordRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // ユーザーを email で検索（status = '1'）
            UserM user = userMRepository.findActiveByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found or inactive: " + username));

            // 2. 有効なパスワードを取得
            UserPassword password = userPasswordRepository
                    .findFirstByUserIdAndIsActiveTrueOrderByCreatedAtDesc(user.getUserId())
                    .orElseThrow(() -> new UsernameNotFoundException("No active password for user: " + username));

            return new UserDetailsImpl(user, password);
        };
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

}
