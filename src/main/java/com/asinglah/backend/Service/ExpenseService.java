package com.asinglah.backend.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.asinglah.backend.DTO.SplitRequest;
import com.asinglah.backend.Entity.Expense;
import com.asinglah.backend.Entity.Expense_group;
import com.asinglah.backend.Entity.Expense_split;
import com.asinglah.backend.Entity.User;
import com.asinglah.backend.Repository.ExpenseGroupRepository;
import com.asinglah.backend.Repository.ExpenseRepository;
import com.asinglah.backend.Repository.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ExpenseGroupRepository expenseGroupRepository;

public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository,ExpenseGroupRepository expenseGroupRepositroy) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.expenseGroupRepository=expenseGroupRepositroy;
    }
@Transactional //this key word faciliates roll back 
public Expense createExpense(Long creatorID,Long groupId,String description, BigDecimal totalAmount, List<SplitRequest> splits) {
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
            split.setRemainingAmountOwed(new BigDecimal(0.0));
            expense.addSplit(split); // sets both sides of the relationship
        }
        return expenseRepository.save(expense); // cascades and saves splits too
    }
}



