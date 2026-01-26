package com.asinglah.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asinglah.backend.DTO.ExpenseGroupResponseDTO.GroupMemberResponseDTO;
import com.asinglah.backend.DTO.ExpenseGroupResponseDTO.ListExpenseGroupDTO;
import com.asinglah.backend.Entity.Expense_group;
import java.util.List;



public interface ExpenseGroupRepository extends JpaRepository<Expense_group,Long>{

    // @Query(value="SELECT",nativeQuery=true)
    // List<ListExpenseGroupDTO> findMemberGroupDTOs(@Param("userId") long userId);

    @Query(value="SELECT eg.group_name AS groupName,eg.expense_group_id AS expenseGroupId FROM expense_group eg LEFT JOIN group_member gm ON eg.expense_group_id = gm.expense_group_id WHERE (gm.user_id = :userId AND gm.statusid = 2) OR eg.group_owner_id = :userId"
    , nativeQuery = true)
    List<ListExpenseGroupDTO> findMemberGroupDTOs(@Param("userId") long userId);

    @Query(value="select gm.user_id as userId,u.email_address as email from expense_group eg join group_member gm on eg.expense_group_id=gm.expense_group_id\r\n" + //
                "join users u on gm.user_id=u.user_id\r\n" + //
                "where eg.expense_group_id=:expenseGroupId", nativeQuery = true)
    List<GroupMemberResponseDTO> findGroupMembersOnExpenseGroupid(@Param("expenseGroupId") long expenseGroupId);

    @Query(value="select eg.group_owner_id as userId,u.email_address as email  from expense_group eg join users u on eg.group_owner_id = u.user_id where eg.expense_group_id=:expenseGroupId",nativeQuery = true)
    GroupMemberResponseDTO findExpenseGroupOwner(@Param("expenseGroupId") long expenseGroupId);

}
