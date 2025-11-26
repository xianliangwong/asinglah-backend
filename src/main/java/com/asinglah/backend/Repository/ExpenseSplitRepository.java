package com.asinglah.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asinglah.backend.Entity.Expense_split;

public interface ExpenseSplitRepository extends JpaRepository<Expense_split,Long> {

    @Query(value = "SELECT * FROM expense_split u WHERE u.expense_id = :groupID", nativeQuery = true)
    Optional<List<Expense_split>> findByGroupID(@Param("groupID" ) long groupID);

}
