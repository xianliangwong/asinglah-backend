package com.asinglah.backend.Controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.asinglah.backend.DTO.ExpenseRequestDTO.CreateExpenseGrp;
import com.asinglah.backend.DTO.ExpenseRequestDTO.CreateExpenseRequest;
import com.asinglah.backend.DTO.ExpenseRequestDTO.CreateExpenseTransactionDTO;
import com.asinglah.backend.DTO.ExpenseRequestDTO.InsertNewSplitDTO;
import com.asinglah.backend.DTO.ExpesenResponseDTO.CreateExpenseGrpResponse;
import com.asinglah.backend.Entity.Expense;
import com.asinglah.backend.Entity.ExpenseTransaction_request;
import com.asinglah.backend.Entity.Expense_split;
import com.asinglah.backend.Entity.group_member;
import com.asinglah.backend.HelperClass.APIResponse;
import com.asinglah.backend.Service.ExpenseService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;





@RestController
@SecurityRequirement(name = "bearerAuth")
public class ExpenseController {
    
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    //ResponseEntity<Expense>
    @PostMapping("/api/expense/v1/expenses")
    public ResponseEntity<APIResponse<Expense>> createExpense(@Valid @RequestBody CreateExpenseRequest request) {
        APIResponse<Expense> response= expenseService.createExpense(
                request.getCreatorId(),
                request.getGroupId(),
                request.getDescription(),
                request.getTotalAmount(),
                request.getSplits()
        );

        return ResponseEntity.status(response.getStatus()).body(response);

        //can change to use a response dto for data in global response 
        //return APIResponse.success(expense);//using global response handler with status code, message and data
        //return ResponseEntity.ok(expense);
    }

    @GetMapping("/api/expense/v1/{expenseGroupId}/expenses")
    public ResponseEntity<APIResponse<List<Expense>>> getAllExpense(@PathVariable Long expenseGroupId) {
       

        APIResponse<List<Expense>> response = expenseService.getAllExpense(expenseGroupId);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
    

    @GetMapping("/api/expense/{expenseId}/splits")
    public ResponseEntity<APIResponse<List<Expense_split>>> getExpenseSplit(@PathVariable Long expenseId) {
        
        APIResponse<List<Expense_split>> response = expenseService.getExistingExpSplit(expenseId);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    //implementation for update existing expense splits 
    @PutMapping("/api/expense/{expenseId}/splits")
    public String updateExistingSplit(@PathVariable Long expenseId,@Valid @RequestBody InsertNewSplitDTO requestNewSplit) {
        //TODO: process POST request
        
        return "";
    }
    

    @PostMapping("/api/expense/{expenseId}/splits")
    public ResponseEntity<APIResponse<List<Expense_split>>> insertNewSplit(@PathVariable Long expenseId,@Valid @RequestBody InsertNewSplitDTO requestNewSplit) {
       
        
        APIResponse<List<Expense_split>> response = expenseService.adjustExistingExpSplit(expenseId,requestNewSplit);
        
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/api/expense/{expenseGroup_id}/groupMember")
    public ResponseEntity<APIResponse<List<group_member>>> insertNewMember(@PathVariable Long groupID,@Valid @RequestBody List<Long> usersID)
    {

        APIResponse<List<group_member>> response = expenseService.insertNewMemberToGroup(groupID, usersID);

        return ResponseEntity.status(response.getStatus()).body(response);


    }

    @PostMapping("/api/expense/{expenseSplitId}/expenseTransaction")
    public ResponseEntity<APIResponse<ExpenseTransaction_request>> insertTransactionRequest(@PathVariable Long expenseSplitId,@RequestBody CreateExpenseTransactionDTO req) {
        
        
        APIResponse<ExpenseTransaction_request> response = expenseService.insertExpenseTransaction(expenseSplitId,req);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/api/expense/{expenseSplitId}/expenseTransaction/{expenseTransactionId}")
    public ResponseEntity<APIResponse<ExpenseTransaction_request>> updateTransactionRequest( @PathVariable("expenseSplitId") Long expenseSplitId,
        @PathVariable("expenseTransactionId") Long expenseTransactionId) {
        
       APIResponse<ExpenseTransaction_request> response = expenseService.updateExpenseTransaction(expenseSplitId, expenseTransactionId);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
    
    
    
    

}
