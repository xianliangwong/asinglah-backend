package com.asinglah.backend.DTO.ExpenseRequestDTO;

import java.math.BigDecimal;

import lombok.Getter;


@Getter
public class SplitRequest {
    private Long userId;
    private BigDecimal amount;
    // getters and setters
}

