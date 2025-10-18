package com.asinglah.backend.DTO.ResponseClass;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpUserResponseDTO {

    private LocalDateTime createdAt; 

    private String createMessgae;
}
