package com.asinglah.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asinglah.backend.DTO.ExpenseGroupResponseDTO.ListExpenseGroupDTO;
import com.asinglah.backend.Entity.Expense_group;
import java.util.List;


public interface ExpenseGroupRepository extends JpaRepository<Expense_group,Long>{

    // @Query(value="SELECT",nativeQuery=true)
    // List<ListExpenseGroupDTO> findMemberGroupDTOs(@Param("userId") long userId);

    @Query(value="SELECT eg.group_name AS groupName,eg.expense_group_id AS expenseGroupId FROM expense_group eg LEFT JOIN group_member gm ON eg.expense_group_id = gm.expense_group_id WHERE (gm.user_id = :userId AND gm.statusid = 2) OR eg.group_owner_id = :userId"
    , nativeQuery = true)
    List<ListExpenseGroupDTO> findMemberGroupDTOs(@Param("userId") long userId);

}
