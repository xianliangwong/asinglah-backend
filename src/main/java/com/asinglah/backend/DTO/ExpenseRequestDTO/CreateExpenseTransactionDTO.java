package com.asinglah.backend.DTO.ExpenseRequestDTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class CreateExpenseTransactionDTO {


    @NotNull
    @Positive
    private BigDecimal totalAmount;

   

    @NotNull
    private Long payeeId;

    @NotNull
    private Long payerId;
}
