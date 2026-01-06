package com.asinglah.backend.Repository;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asinglah.backend.DTO.ExpenseGroupResponseDTO.GroupInvResponseDTO;
import com.asinglah.backend.Entity.group_member;

public interface GroupMemberRepository extends JpaRepository<group_member,Long>{

    //jpql implementation 
    // @Query("SELECT new com.asinglah.backend.DTO.ExpesenResponseDTO.ListExpenseGroupDTO(eg.groupName, eg.expenseGroupId) " 
    // + "FROM group_member gm RIGHT JOIN gm.expense_group eg WHERE (gm.user_id.id = :userId AND gm.statusID.statusID =2) OR (eg.groupOwnerId.id=:userId)") 
    // List<ListExpenseGroupDTO> findMemberGroupDTOs(@Param("userId") long userId);

    @Query(value=
        "SELECT eg.group_name AS groupName,eg.expense_group_id AS expenseGroupId, u.email_address AS invitor,gm.created_at AS invitationDateTime"
        +" FROM group_member gm join expense_group eg on gm.expense_group_id=eg.expense_group_id" 
        +" JOIN users u on eg.group_owner_id=u.user_id"
        +" WHERE gm.user_id=:userId AND gm.statusid=1"
    , nativeQuery = true)
    List<GroupInvResponseDTO> findGroupMemberInv(@Param("userId") long userId);


}
