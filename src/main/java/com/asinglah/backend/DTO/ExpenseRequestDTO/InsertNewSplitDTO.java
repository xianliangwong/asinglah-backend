package com.asinglah.backend.DTO.ExpenseRequestDTO;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InsertNewSplitDTO {

    @NotNull
    private List<SplitRequest> newExpenseSplit;

    @NotNull
    private List<existingSplitDTO> existingExpenseSplit;

    @NotNull
    private long expenseId;

    

}
