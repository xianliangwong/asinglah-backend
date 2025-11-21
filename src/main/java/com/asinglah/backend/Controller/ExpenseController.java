package com.asinglah.backend.Controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.asinglah.backend.DTO.ExpenseRequestDTO.CreateExpenseGrp;
import com.asinglah.backend.DTO.ExpenseRequestDTO.CreateExpenseRequest;
import com.asinglah.backend.DTO.ExpenseRequestDTO.InsertNewSplitDTO;
import com.asinglah.backend.DTO.ExpesenResponseDTO.CreateExpenseGrpResponse;
import com.asinglah.backend.Entity.Expense;
import com.asinglah.backend.Entity.Expense_group;
import com.asinglah.backend.HelperClass.APIResponse;
import com.asinglah.backend.Service.ExpenseService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class ExpenseController {
    
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    //ResponseEntity<Expense>
    @PostMapping("/api/expense/createExpense")
    public ResponseEntity<APIResponse<Expense>> createExpense(@Valid @RequestBody CreateExpenseRequest request) {
        APIResponse<Expense> response= expenseService.createExpense(
                request.getCreatorId(),
                request.getGroupId(),
                request.getDescription(),
                request.getTotalAmount(),
                request.getSplits()
        );

        return ResponseEntity.ok(response);

        //can change to use a response dto for data in global response 
        //return APIResponse.success(expense);//using global response handler with status code, message and data
        //return ResponseEntity.ok(expense);
    }

    @PostMapping("/api/expense/createExpenseGroup")
    public APIResponse<CreateExpenseGrpResponse> createExpenseGroupID(@Valid @RequestBody CreateExpenseGrp request) {
        
        
        Expense_group expense_group = expenseService.createExpenseGroup(request);

        CreateExpenseGrpResponse response = 
        new CreateExpenseGrpResponse(expense_group.getCreatedAt(),"expense group created", 
        expense_group.getGroupName(), expense_group.getExpenseGroupId());

        return APIResponse.success(response);

        
    }

    @GetMapping("/api/expense/getExpenseSplit")
    public String getExpenseSplit(@RequestParam String param) {
        return new String();
    }

    @PostMapping("/api/expense/update/adjustSplit")
    public String updateExistingSplit(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    

    @PostMapping("/api/expense/{expenseId}/splits")
    public APIResponse<String> insertNewSplit(@PathVariable Long expenseId,@Valid @RequestBody InsertNewSplitDTO requestNewSplit) {
       
        
        expenseService.adjustExistingExpSplit(expenseId,requestNewSplit);
        
        return APIResponse.successCreate("new split created and adjusted existing expense split");
    }
    
    
    

}
