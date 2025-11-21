package com.asinglah.backend.DTO.ExpenseRequestDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateExpenseGrp {


    @NotNull
    private String groupName;

    @NotNull
    private long groupOwnerId;


}
