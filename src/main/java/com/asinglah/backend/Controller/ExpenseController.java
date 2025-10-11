package com.asinglah.backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.asinglah.backend.DTO.CreateExpenseRequest;
import com.asinglah.backend.Entity.Expense;
import com.asinglah.backend.Service.ExpenseService;

import jakarta.validation.Valid;

@RestController
public class ExpenseController {
    
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/api/expense/createExpense")
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody CreateExpenseRequest request) {
        Expense expense = expenseService.createExpense(
                request.getCreatorId(),
                request.getGroupId(),
                request.getDescription(),
                request.getTotalAmount(),
                request.getSplits()
        );
        return ResponseEntity.ok(expense);
    }

}
