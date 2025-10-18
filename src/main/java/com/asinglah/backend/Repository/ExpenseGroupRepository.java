package com.asinglah.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asinglah.backend.Entity.Expense_group;

public interface ExpenseGroupRepository extends JpaRepository<Expense_group,Long>{

}
