package com.asinglah.backend.DTO.UserRequestDTO;

import jakarta.validation.constraints.Email;

public record SignInRequestDTO(

    @Email String emailAddress,
    String password

) {

}
