package com.asinglah.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asinglah.backend.Entity.Expense;


public interface ExpenseRepository extends JpaRepository<Expense,Long>{

    @Query(value = "SELECT * FROM expense e WHERE e.group_id = :groupId", nativeQuery = true)
    Optional<List<Expense>> getAllExpense(@Param("groupId") Long groupId);

}
