package com.asinglah.backend.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SignInDTO {


    @NotNull
    private String emailAddress;

    @NotNull
    private String password;

}
