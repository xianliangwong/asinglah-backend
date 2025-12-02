package com.asinglah.backend.DTO.UserRequestDTO;

import jakarta.validation.constraints.Email;

public record ResetPasswordDTO(

    @Email String emailAddress,
    String newPassword
) {

}
