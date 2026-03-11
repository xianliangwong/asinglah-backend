package com.asinglah.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asinglah.backend.DTO.ExpenseResponseDTO.ExpenseOweResDTO;
import com.asinglah.backend.DTO.ExpenseResponseDTO.OweExpensesDetailResDTO;
import com.asinglah.backend.Entity.Expense;


public interface ExpenseRepository extends JpaRepository<Expense,Long>{

    @Query(value = "SELECT * FROM expense e WHERE e.group_id = :groupId", nativeQuery = true)
    Optional<List<Expense>> getAllExpense(@Param("groupId") Long groupId);

    @Query(value = "select u.email_address as name, SUM(es.remaining_amount_owed) as amount from expense e join expense_split es on e.expense_id=es.expense_id and e.init_payer_id !=:userId join users u on e.init_payer_id=u.user_id where e.group_id = :groupId and es.is_settled=0 and es.participant_user_id=:userId group by e.init_payer_id,u.email_address"  
    , nativeQuery = true)
    Optional<List<ExpenseOweResDTO>> getOweExpense(@Param("userId") Long userId,@Param("groupId") Long groupId);

    @Query(value ="select e.description as expenseDetails, es.remaining_amount_owed as amount, case when etr.statusid is null then 0 when etr.statusid = 3 then 0 else 1 end as pendingStatus, es.expense_split_id as expenseSplitId from expense e join expense_split es on e.expense_id=es.expense_id and e.init_payer_id !=:userId  left join expense_transaction_request etr on es.expense_split_id =etr.expense_split_id where e.group_id = :groupId and es.is_settled=0 and es.participant_user_id=:userId and e.init_payer_id=:initPayerId", nativeQuery = true)
    Optional<List<OweExpensesDetailResDTO>> getOweExpenseByInitPayerId(@Param("initPayerId") Long initPayerId,@Param("userId") Long userId,@Param("groupId") Long groupId);

}
