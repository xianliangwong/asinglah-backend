package com.asinglah.backend.DTO.ExpenseRequestDTO;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class CreateExpenseRequest {
    @NotNull
    private Long creatorId;

    @NotNull 
    private Long payerId;
    
    @NotNull
    private Long groupId;

    @NotNull
    private String description;

    @NotNull
    @Positive
    private BigDecimal totalAmount;

    @NotNull
    private LocalDate transactionDate;

    @NotNull
    @Valid
    private List<SplitRequest> splits;
}

