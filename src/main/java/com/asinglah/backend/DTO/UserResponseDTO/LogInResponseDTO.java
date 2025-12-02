package com.asinglah.backend.DTO.UserResponseDTO;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;

public record LogInResponseDTO(

    @Email String emailAddress,
    String accessToken,
    LocalDateTime createdAt
) {

} 