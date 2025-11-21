package com.asinglah.backend.DTO.ExpenseRequestDTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class existingSplitDTO {


    @NotNull
    private long splitId;
    
    @NotNull
    private BigDecimal adjustedAmt;

}
