package com.ginotoro.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ginotoro.backend.config.ApplicationProperties;
import com.ginotoro.backend.dto.AuthUserDto;
import com.ginotoro.backend.dto.LoginUserDto;
import com.ginotoro.backend.dto.RegisterUserDto;
import com.ginotoro.backend.entity.UserM;
import com.ginotoro.backend.security.UserDetailsImpl;
import com.ginotoro.backend.service.AuthService;
import com.ginotoro.backend.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthService authService;

    @Autowired
    ApplicationProperties properties;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto req) {
        try {
            authService.signup(req);

            return ResponseEntity.ok(Map.of("accessToken", ""));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Signup failed"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto req, HttpServletResponse res) {

        try {
            // 認証処理
            Authentication authentication = (Authentication) authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
            UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
            String accessToken = jwtService.generateAccessToken(user.getUsername());

            jwtService.setRefreshToken(user.getUsername(), res);

            return ResponseEntity.ok(Map.of("accessToken", accessToken));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login failed"));
        }

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "refreshAPI : No refresh token"));
        }
        try {

            String email = jwtService.getEmail(refreshToken);
            String newAccessToken = jwtService.generateAccessToken(email);
            System.out.println("refreshメソッド newAccessToken： " + newAccessToken);

            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "refreshAPI : Access token expired"));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "refreshAPI : Invalid refresh token"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "refreshAPI : Server error"));
        }

    }

    @GetMapping("/user")
    public ResponseEntity<?> user(@AuthenticationPrincipal UserDetails userDetails,
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {
        boolean accessTokenExists = true;

        if (userDetails == null) {
            if (refreshToken == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "userAPI : No refresh token"));
            }
            accessTokenExists = false;
        }

        try {
            UserM userInfo = new UserM();
            if (accessTokenExists) {
                userInfo = authService.getUserInfo(userDetails.getUsername());
            } else {
                userInfo = authService.getUserInfo(jwtService.getEmail(refreshToken));
            }
            AuthUserDto userDto = new AuthUserDto();
            userDto.setEmail(userInfo.getEmail());
            userDto.setDisplayname(userInfo.getDisplayName());
            String newAccessToken = jwtService.generateAccessToken(userInfo.getEmail());

            return ResponseEntity.ok(Map.of("user", userDto, "accessToken", newAccessToken));

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "userAPI ：Access token expired"));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "userAPI ：Invalid refresh token"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "userAPI ：Server error"));
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Cookie を削除（有効期限0にする）
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }

}
