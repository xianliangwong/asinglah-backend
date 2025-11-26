package com.asinglah.backend.Controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.asinglah.backend.DTO.ExpenseRequestDTO.CreateExpenseGrp;
import com.asinglah.backend.DTO.ExpenseRequestDTO.CreateExpenseRequest;
import com.asinglah.backend.DTO.ExpenseRequestDTO.InsertNewSplitDTO;
import com.asinglah.backend.DTO.ExpesenResponseDTO.CreateExpenseGrpResponse;
import com.asinglah.backend.Entity.Expense;
import com.asinglah.backend.Entity.Expense_group;
import com.asinglah.backend.Entity.Expense_split;
import com.asinglah.backend.Entity.group_member;
import com.asinglah.backend.HelperClass.APIResponse;
import com.asinglah.backend.Service.ExpenseService;

import jakarta.validation.Valid;

import java.util.List;

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

    @PostMapping("/api/expense/v1/expenseGroup")
    public ResponseEntity<APIResponse<CreateExpenseGrpResponse>> createExpenseGroupID(@Valid @RequestBody CreateExpenseGrp request) {
        
        
        APIResponse<CreateExpenseGrpResponse> response = expenseService.createExpenseGroup(request);

        return ResponseEntity.status(response.getStatus()).body(response);

        
    }

    @GetMapping("/api/expense/{expenseId}/splits")
    public ResponseEntity<APIResponse<List<Expense_split>>> getExpenseSplit(@PathVariable Long expenseId) {
        
        APIResponse<List<Expense_split>> response = expenseService.getExistingExpSplit(expenseId);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    //change to use update action verb 
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
    
    
    

}
