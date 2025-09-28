package com.ginotoro.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDto {

    @NotBlank
    @NotNull
    private String email;

    @NotBlank
    @NotNull
    private String password;
}
