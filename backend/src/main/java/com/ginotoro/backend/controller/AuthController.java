package com.ginotoro.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ginotoro.backend.config.ApplicationProperties;
import com.ginotoro.backend.dto.AuthUserDto;
import com.ginotoro.backend.dto.LoginUserDto;
import com.ginotoro.backend.dto.RegisterUserDto;
import com.ginotoro.backend.entity.UserM;
import com.ginotoro.backend.entity.UserPassword;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
// @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthService authService;

    @Autowired
    ApplicationProperties properties;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto req) {

        System.out.println(
                "displayname=" + req.getDisplayname() + ", email=" + req.getEmail() + ", password="
                        + req.getPassword());
        authService.signup(req);

        return ResponseEntity.ok(Map.of("accessToken", ""));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto req, HttpServletResponse response) {

        System.out.println("username=" + req.getEmail() + ", password=" + req.getPassword());

        try {
            authService.authenticate(req);

            UserDetailsImpl user = new UserDetailsImpl(new UserM(), new UserPassword());
            String accessToken = jwtService.generateAccessToken(user.getUsername());
            String refreshToken = jwtService.generateRefreshToken(user.getUsername());

            // Refrese Tokenを Cookieに保存(httpOnly)
            ResponseCookie cookie = ResponseCookie
                    .from("refreshToken", refreshToken)
                    .httpOnly(true)
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
        if (jwtService.isTokenValid(refreshToken)) {

            String email = jwtService.getEmail(refreshToken);

            String newAccessToken = jwtService.generateAccessToken(email);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/user")
    public ResponseEntity<?> user(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        try {

            UserM userInfo = authService.getUserInfo(userDetails.getUsername());
            AuthUserDto res = new AuthUserDto();
            res.setEmail(userInfo.getEmail());
            res.setDisplayname(userInfo.getDisplayName());

            return ResponseEntity.ok(res);
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
