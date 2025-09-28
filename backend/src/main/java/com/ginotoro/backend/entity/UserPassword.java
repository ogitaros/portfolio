package com.ginotoro.backend.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_password")
@Getter
@Setter
public class UserPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "password_id")
    private Integer passwordId;

    @Column(name = "user_id")
    @NotNull
    private Integer userId;

    @Column(name = "password_hash", length = 300)
    @NotNull
    private String passwordHash;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "creator", length = 100)
    private String creator;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updater", length = 100)
    private String updater;

    @Version
    @Column(name = "version_no")
    private Integer versionNo;
}
