package com.asinglah.backend.DTO.ExpenseGroupResponseDTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateExpenseGrpResponse {

    private LocalDateTime createdAt; 

    private String createMessgae;

    private String groupName;
    
    private Long groupId;
}
