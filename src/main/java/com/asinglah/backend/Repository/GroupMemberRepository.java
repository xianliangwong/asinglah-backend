package com.asinglah.backend.Repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asinglah.backend.DTO.ExpesenResponseDTO.ListExpenseGroupDTO;
import com.asinglah.backend.Entity.group_member;

public interface GroupMemberRepository extends JpaRepository<group_member,Long>{

    //jpql implementation 
    @Query("SELECT new com.asinglah.backend.DTO.ExpesenResponseDTO.ListExpenseGroupDTO(eg.groupName, eg.expenseGroupId) " 
    + "FROM group_member gm JOIN gm.expense_group eg WHERE gm.user_id.id = :userId AND gm.statusID.statusID =2") 
    List<ListExpenseGroupDTO> findMemberGroupDTOs(@Param("userId") long userId);



}
