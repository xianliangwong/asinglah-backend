package com.asinglah.backend.Service;

import java.io.ObjectInputFilter.Status;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.asinglah.backend.DTO.ExpenseRequestDTO.CreateExpenseGrp;
import com.asinglah.backend.DTO.ExpenseRequestDTO.InsertNewSplitDTO;
import com.asinglah.backend.DTO.ExpenseRequestDTO.SplitRequest;
import com.asinglah.backend.DTO.ExpenseRequestDTO.existingSplitDTO;
import com.asinglah.backend.Entity.Expense;
import com.asinglah.backend.Entity.Expense_group;
import com.asinglah.backend.Entity.Expense_split;
import com.asinglah.backend.Entity.User;
import com.asinglah.backend.HelperClass.APIResponse;
import com.asinglah.backend.Repository.ExpenseGroupRepository;
import com.asinglah.backend.Repository.ExpenseRepository;
import com.asinglah.backend.Repository.ExpenseSplitRepository;
import com.asinglah.backend.Repository.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ExpenseGroupRepository expenseGroupRepository;
    private final ExpenseSplitRepository expenseSplitRepository;

public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository,ExpenseGroupRepository expenseGroupRepositroy,
ExpenseSplitRepository expenseSplitRepository
) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.expenseGroupRepository=expenseGroupRepositroy;
        this.expenseSplitRepository=expenseSplitRepository;
    }
@Transactional //this key word faciliates roll back 
public APIResponse<Expense> createExpense(Long creatorID,Long groupId,String description, BigDecimal totalAmount, List<SplitRequest> splits) {
        Expense expense = new Expense();
        expense.setDescription(description);
        expense.setTotalAmount(totalAmount);
        expense.setCurrencyCode("MYR");

        Expense_group expenseGroup = expenseGroupRepository.findById(groupId)
                    .orElseThrow(() -> new RuntimeException("Group ID not found"));
                    
        
        expense.setExpense_group(expenseGroup);

        User creatorUser = userRepository.findById(creatorID).orElseThrow(() -> new RuntimeException("user not found"));

        expense.setUser((creatorUser));

        for (SplitRequest splitReq : splits) {
            User user = userRepository.findById(splitReq.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Expense_split split = new Expense_split();
            split.setTotalAmountOwed(splitReq.getAmount());
            split.setSettled(false);
            split.setPartipcantUser(user);
            split.setRemainingAmountOwed(splitReq.getAmount());
            expense.addSplit(split); // sets both sides of the relationship
        }

        try{
            Expense resultExpense= expenseRepository.save(expense); // cascades and saves splits too

            return APIResponse.success(resultExpense);
        }
        catch(Exception e){
             return APIResponse.failure("Failed to create new expense item: " + e.getMessage());
            
        }

        
    }

    @Transactional
    public Expense_group createExpenseGroup(CreateExpenseGrp newGroup)
    {

        Expense_group expenseGroup = new Expense_group();


        User groupOwnerID = userRepository.findById(newGroup.getGroupOwnerId())
        .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND, "User not found"
        ));

        expenseGroup.setGroupOwnerId(groupOwnerID);
        expenseGroup.setGroupName(newGroup.getGroupName());

        return expenseGroupRepository.save(expenseGroup);
       
        
    }

    @Transactional
    public boolean adjustExistingExpSplit(long expenseID,InsertNewSplitDTO requestSplit){

        List<existingSplitDTO> existingSplitDTO = requestSplit.getExistingExpenseSplit();
        List<SplitRequest> newSplitDTO = requestSplit.getNewExpenseSplit();


        

        //can only be adjusted for haven't settled expense split ?

        try {
            for (existingSplitDTO split : existingSplitDTO) {

            long splitID=split.getSplitId();

            

            Expense_split expenseSplit = expenseSplitRepository.findById(splitID)
            .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "User not found"
            ));;

            

            expenseSplit.setTotalAmountOwed(split.getAdjustedAmt());       
            
        }

        for (SplitRequest splitRequest : newSplitDTO) {
            
            Expense_split newSplit = new Expense_split();
            User user = userRepository.findById(splitRequest.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
            Expense expense = expenseRepository.findById(expenseID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"expense not found"));
            newSplit.setPartipcantUser(user);
            newSplit.setRemainingAmountOwed(splitRequest.getAmount());
            newSplit.setExpenseId(expense);
            newSplit.setTotalAmountOwed(splitRequest.getAmount());
            newSplit.setSettled(false);
            
            expenseSplitRepository.save(newSplit);
            
            

        }

        return true;
            
        } catch (Exception e) {

             throw new ResponseStatusException(
               HttpStatus.INTERNAL_SERVER_ERROR,
          "failed to create adjusted expense split"
            );
            
            
        }
      

        //add the new split request 

        
    }

    

    
}





