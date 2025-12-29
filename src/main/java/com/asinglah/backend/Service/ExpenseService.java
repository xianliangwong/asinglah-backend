package com.asinglah.backend.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.asinglah.backend.DTO.ExpenseRequestDTO.CreateExpenseGrp;
import com.asinglah.backend.DTO.ExpenseRequestDTO.CreateExpenseTransactionDTO;
import com.asinglah.backend.DTO.ExpenseRequestDTO.InsertNewSplitDTO;
import com.asinglah.backend.DTO.ExpenseRequestDTO.SplitRequest;
import com.asinglah.backend.DTO.ExpenseRequestDTO.existingSplitDTO;
import com.asinglah.backend.DTO.ExpesenResponseDTO.CreateExpenseGrpResponse;
import com.asinglah.backend.DTO.ExpesenResponseDTO.ListExpenseGroupDTO;
import com.asinglah.backend.Entity.Expense;
import com.asinglah.backend.Entity.ExpenseTransaction_request;
import com.asinglah.backend.Entity.Expense_group;
import com.asinglah.backend.Entity.Expense_split;
import com.asinglah.backend.Entity.StatusCode;
import com.asinglah.backend.Entity.User;
import com.asinglah.backend.Entity.group_member;
import com.asinglah.backend.HelperClass.APIResponse;
import com.asinglah.backend.Repository.ExpenseGroupRepository;
import com.asinglah.backend.Repository.ExpenseRepository;
import com.asinglah.backend.Repository.ExpenseSplitRepository;
import com.asinglah.backend.Repository.ExpenseTranReqRepository;
import com.asinglah.backend.Repository.GroupMemberRepository;
import com.asinglah.backend.Repository.StatusCodeRepository;
import com.asinglah.backend.Repository.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ExpenseGroupRepository expenseGroupRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final StatusCodeRepository statusCodeRepository;
    private final ExpenseTranReqRepository expenseTranReqRepository;

    private String newGroupCreationStatus ="PENDING";
    private String transactionRequestStatus ="PENDING";
    private String successTransaction ="SUCCESS";

public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository,ExpenseGroupRepository expenseGroupRepositroy,
ExpenseSplitRepository expenseSplitRepository,GroupMemberRepository groupMemberRepository,StatusCodeRepository statusCodeRepository,
ExpenseTranReqRepository expenseTranReqRepository
) 
{
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.expenseGroupRepository=expenseGroupRepositroy;
        this.expenseSplitRepository=expenseSplitRepository;
        this.groupMemberRepository=groupMemberRepository;
        this.statusCodeRepository=statusCodeRepository;
        this.expenseTranReqRepository=expenseTranReqRepository;
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

            return APIResponse.successCreate(resultExpense);
        }
        catch(Exception e){
             return APIResponse.failure("Failed to create new expense item: " + e.getMessage());
            
        }

        
    }

    @Transactional
    public APIResponse<List<Expense>> getAllExpense(Long groupID)
    {

        
        List<Expense> listOfExpense = expenseRepository.getAllExpense(groupID)
        .filter(list -> !list.isEmpty())
        .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND, "no result found with this expense ID: "+groupID
        ));

        return APIResponse.success(listOfExpense);

    }

    @Transactional
    public APIResponse<CreateExpenseGrpResponse> createExpenseGroup(CreateExpenseGrp newGroup)
    {

        Expense_group expenseGroup = new Expense_group();


        User groupOwnerID = userRepository.findById(newGroup.getGroupOwnerId())
        .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND, "User not found"
        ));

        expenseGroup.setGroupOwnerId(groupOwnerID);
        expenseGroup.setGroupName(newGroup.getGroupName());


        try{
        Expense_group saveResponse= expenseGroupRepository.save(expenseGroup);

        //statusID of the member
        StatusCode statusCode = statusCodeRepository.findByStatusDesc(newGroupCreationStatus)
        .orElseThrow(() -> new RuntimeException("StatusCode not found"));

        try{
        if(newGroup.getListOfMembers().size()>0)
        {
             for(long userId:newGroup.getListOfMembers())
            {
                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
                
                group_member groupMember = new group_member();
                groupMember.setUser_id(user);
                groupMember.setExpense_group(saveResponse);
                groupMember.setStatusID(statusCode);

                groupMemberRepository.save(groupMember);
               

            }

        }
        }
        catch(Exception e){
            return APIResponse.failure("Failed to insert new member to expense group: " + e.getMessage());
        }
    

      
       
        CreateExpenseGrpResponse response = 
        new CreateExpenseGrpResponse(saveResponse.getCreatedAt(),"expense group created", 
        saveResponse.getGroupName(), saveResponse.getExpenseGroupId());

        return APIResponse.successCreate(response);

        }
        catch(Exception e)
        {
             return APIResponse.failure("Failed to create new expense group: " + e.getMessage());

        }
        
    }

    @Transactional
    public APIResponse<List<Expense_split>> adjustExistingExpSplit(long expenseID,InsertNewSplitDTO requestSplit){

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


        List<Expense_split> listResponse= new ArrayList<>();

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
            
             listResponse.add(expenseSplitRepository.save(newSplit));
            
           

        }

       return APIResponse.successCreate(listResponse);
            
        } catch (Exception e) {


            return APIResponse.failure("Failed to add new splits: " + e.getMessage());
        //      throw new ResponseStatusException(
        //        HttpStatus.INTERNAL_SERVER_ERROR,
        //   "failed to create adjusted expense split"
        //     );
            
            
        }
      

        //add the new split request 

        
    }

    @Transactional
    public APIResponse<List<Expense_split>> getExistingExpSplit(long expenseID)
    {

        
        List<Expense_split> expenseSplitList = expenseSplitRepository.findByGroupID(expenseID)
        .filter(list -> !list.isEmpty())
        .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND, "no result found with this group ID: "+expenseID
        ));

        return APIResponse.success(expenseSplitList);


        



    }
    

    @Transactional
    public APIResponse<List<group_member>> insertNewMemberToGroup(Long groupID,List<Long> usersID)
    {


        List<group_member> responseGroupMember = new ArrayList<>();

        try{

            Expense_group expesenGroup = expenseGroupRepository.findById(groupID)
            .orElseThrow(() -> new RuntimeException("Group not found"));

            for(long userID:usersID){

                group_member newGroupMember = new group_member();

                User memberID = userRepository.findById(userID)
                 .orElseThrow(() -> new RuntimeException("User not found"));

                StatusCode statusID = statusCodeRepository.findByStatusDesc(newGroupCreationStatus)
                .orElseThrow(() -> new RuntimeException("StatusID not found"));
                

                newGroupMember.setExpense_group(expesenGroup);
                newGroupMember.setUser_id(memberID);
                newGroupMember.setStatusID(statusID);

                group_member result= groupMemberRepository.save(newGroupMember);

                responseGroupMember.add(result);


            }

            return APIResponse.successCreate(responseGroupMember);

        }
        catch(Exception e){

            return APIResponse.failure("Failed to add new member to group expense: " + e.getMessage());
        }

      
    }
    
    @Transactional
    public APIResponse<ExpenseTransaction_request> insertExpenseTransaction(Long expenseSplitId,CreateExpenseTransactionDTO req)
    {

        try{

            ExpenseTransaction_request expenseTranReq = new ExpenseTransaction_request();

            User payee = userRepository.findById(req.getPayeeId()).orElseThrow(() -> new RuntimeException("payee doesn't exists"));

            User payer = userRepository.findById(req.getPayerId()).orElseThrow(() -> new RuntimeException("payee doesn't exists"));

            StatusCode statusId =statusCodeRepository.findByStatusDesc(transactionRequestStatus)
            .orElseThrow(() -> new RuntimeException("statusID doesn't exists"));

            Expense_split exp = expenseSplitRepository.findById(expenseSplitId).orElseThrow(() -> new RuntimeException("expense id doesn't exists"));

            if(payer != payee){

            expenseTranReq.setPayeeId(payee);
            expenseTranReq.setPayerId(payer);
            expenseTranReq.setStatusID(statusId);
            expenseTranReq.setAmountPaid(req.getTotalAmount());
            expenseTranReq.setExpenseSplitId(exp);

            expenseTranReqRepository.save(expenseTranReq);
            
            return APIResponse.successCreate(expenseTranReq);

            }
            else
            {
                return APIResponse.failure("payee user id error");
            }

            
        }
        catch(Exception e)
        {
            return APIResponse.failure("Failed to add expense transaction: " + e.getMessage());
        }

    }

    @Transactional
    public APIResponse<ExpenseTransaction_request> updateExpenseTransaction(Long expenseSplitId,Long expenseTransactionId)
    {
        
       

        try{
        ExpenseTransaction_request trans = expenseTranReqRepository.findById(expenseTransactionId)
        .orElseThrow(() -> new RuntimeException("expense transaction request not found"));

        StatusCode statusCode = statusCodeRepository.findByStatusDesc(successTransaction)
        .orElseThrow(() -> new RuntimeException("status id not found"));

        

        trans.setStatusID(statusCode);
        trans.setUpdatedAt(LocalDateTime.now());
        

        ExpenseTransaction_request response =expenseTranReqRepository.save(trans);

        //step 2 to update expense split 
        Expense_split expSplit = expenseSplitRepository.findById(expenseSplitId)
        .orElseThrow(() -> new RuntimeException("expense split not found"));

        BigDecimal remaingAmountOwed=expSplit.getTotalAmountOwed().subtract(trans.getAmountPaid());
       

        expSplit.setUpdatedAt(LocalDateTime.now());
        expSplit.setRemainingAmountOwed(remaingAmountOwed);
        if(remaingAmountOwed.compareTo(BigDecimal.ZERO)==0){
            expSplit.setSettled(true);

        }

        expenseSplitRepository.save(expSplit);


        return APIResponse.success(response);
        }
        catch(Exception e){
            return APIResponse.failure("Failed to update expense transaction: " + e.getMessage());
        }
    }


    @Transactional
    public APIResponse<List<ListExpenseGroupDTO>> getExpenseGroupByUserId(Long userId)
    {

        try{

            List<ListExpenseGroupDTO> responseDTO = expenseGroupRepository.findMemberGroupDTOs(userId);

            if(responseDTO.size()==0||responseDTO==null){
               throw new ResponseStatusException(HttpStatus.NOT_FOUND,"invalid userId");
            }
           

            return APIResponse.success(responseDTO);
        }
        catch(Exception e){
            return APIResponse.failure("failed to get expense groups");
           
        }



    }
}





