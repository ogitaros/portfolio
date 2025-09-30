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

import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

        authService.signup(req);

        return ResponseEntity.ok(Map.of("accessToken", ""));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto req, HttpServletResponse response) {

        try {
            // 認証処理
            Authentication authentication = (Authentication) authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
            UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
            String accessToken = jwtService.generateAccessToken(user.getUsername());
            String refreshToken = jwtService.generateRefreshToken(user.getUsername());

            // Refrese Tokenを Cookieに保存(httpOnly)
            ResponseCookie cookie = ResponseCookie
                    .from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false) // dev環境なのでfalse、本番はtrue
                    .sameSite("Lax") // dev環境なのでNone、本番は?
                    .path("/")
                    .maxAge(properties.getRefreshExpiration())
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok(Map.of("accessToken", accessToken));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") String refreshToken) {

        System.out.println("refreshメソッド：" + refreshToken);
        if (refreshToken == null) {
            System.out.println("refreshメソッド： No refresh token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No refresh token");
        }
        if (!jwtService.isTokenValid(refreshToken)) {
            System.out.println("refreshメソッド： Invalid refresh token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String email = jwtService.getEmail(refreshToken);
        String newAccessToken = jwtService.generateAccessToken(email);
        System.out.println("refreshメソッド newAccessToken： " + newAccessToken);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));

    }

    @GetMapping("/user")
    public ResponseEntity<?> user(@AuthenticationPrincipal UserDetails userDetails,
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {
        boolean accessTokenExists = true;

        if (userDetails == null) {
            if (refreshToken == null) {
                System.out.println("userAPI： No refresh token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No access / refresh token");
            }
            if (!jwtService.isTokenValid(refreshToken)) {
                System.out.println("userAPI： Invalid refresh token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No access token / Invalid refresh token");
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

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
