package com.ginotoro.backend.security;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ginotoro.backend.entity.UserM;
import com.ginotoro.backend.entity.UserPassword;

public class UserDetailsImpl implements UserDetails {

    private final UserM user;
    private final UserPassword password;

    public UserDetailsImpl(UserM user, UserPassword password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        // 実際は user_password テーブルから取得するようにする
        return password.getPasswordHash();
    }

    @Override
    public boolean isAccountNonExpired() {
        LocalDateTime expiresAt = password.getExpiresAt();
        if (Objects.isNull(expiresAt) || expiresAt.isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"3".equals(user.getStatus()); // 例: status='3'ならロック
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // パスワードの有効期限で制御するならここを実装
    }

    @Override
    public boolean isEnabled() {
        return !"4".equals(user.getStatus()); // 例: status="4"なら無効
    }

}
