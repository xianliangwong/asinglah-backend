package com.asinglah.backend.Controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.asinglah.backend.DTO.CreateExpenseRequest;
import com.asinglah.backend.Entity.Expense;
import com.asinglah.backend.HelperClass.APIResponse;
import com.asinglah.backend.Service.ExpenseService;

import jakarta.validation.Valid;

@RestController
public class ExpenseController {
    
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    //ResponseEntity<Expense>
    @PostMapping("/api/expense/createExpense")
    public APIResponse<Expense> createExpense(@Valid @RequestBody CreateExpenseRequest request) {
        Expense expense = expenseService.createExpense(
                request.getCreatorId(),
                request.getGroupId(),
                request.getDescription(),
                request.getTotalAmount(),
                request.getSplits()
        );

        //can change to use a response dto for data in global response 
        return APIResponse.success(expense);//using global response handler with status code, message and data
        //return ResponseEntity.ok(expense);
    }

}
