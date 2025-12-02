package com.asinglah.backend.DTO.UserResponseDTO;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;

public record ResetPasswordResponseDTO(

    @Email String emailAddress,
    LocalDateTime createTime

) {
} 
