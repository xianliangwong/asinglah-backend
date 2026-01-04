package com.asinglah.backend.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.asinglah.backend.DTO.ExpenseGroupResponseDTO.CreateExpenseGrpResponse;
import com.asinglah.backend.DTO.ExpenseGroupResponseDTO.GroupInvResponseDTO;
import com.asinglah.backend.DTO.ExpenseGroupResponseDTO.ListExpenseGroupDTO;
import com.asinglah.backend.DTO.ExpenseRequestDTO.CreateExpenseGrp;
import com.asinglah.backend.HelperClass.APIResponse;
import com.asinglah.backend.Service.ExpenseService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@SecurityRequirement(name = "bearerAuth")
public class ExpenseGroupController {

    private final ExpenseService expenseService;

    public ExpenseGroupController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/api/expenseGroup")
    public ResponseEntity<APIResponse<CreateExpenseGrpResponse>> createExpenseGroupID(@Valid @RequestBody CreateExpenseGrp request) {
        
        
        APIResponse<CreateExpenseGrpResponse> response = expenseService.createExpenseGroup(request);

        return ResponseEntity.status(response.getStatus()).body(response);

        
    }

    @GetMapping("/api/expenseGroup/{expenseGroupId}")
    public String getExpenseGroup(@RequestParam String param) {
        return new String();
    }

    @GetMapping("/api/expenseGroup/user")
    public ResponseEntity<APIResponse<List<ListExpenseGroupDTO>>> getExpenseGroupsByUserId(@RequestParam long userId) {

        APIResponse<List<ListExpenseGroupDTO>> response=expenseService.getExpenseGroupByUserId(userId);

         return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/api/expenseGroup/invitation")
    public ResponseEntity<APIResponse<List<GroupInvResponseDTO>>> getInvitationListByUserId(@RequestParam long userId) {
        APIResponse<List<GroupInvResponseDTO>> response=expenseService.getGroupInvitationListByUserId(userId);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
    

    @PutMapping("/api/expenseGroup/invitation")
    public String updateGroupMember(@RequestBody String entity) {
        //TODO: process PUT request
        
        return entity;
    }
    
    


    

}
