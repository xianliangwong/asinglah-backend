package com.asinglah.backend.DTO;

import java.math.BigDecimal;
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
    private Long groupId;

    @NotNull
    private String description;

    @NotNull
    @Positive
    private BigDecimal totalAmount;

    @NotNull
    @Valid
    private List<SplitRequest> splits;
}

