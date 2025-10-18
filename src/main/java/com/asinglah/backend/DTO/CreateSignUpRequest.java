package com.asinglah.backend.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateSignUpRequest {

    @NotNull
    private String emailAddress;
    
    @NotNull
    private String fullName;

    @NotNull
    private String password;

}
