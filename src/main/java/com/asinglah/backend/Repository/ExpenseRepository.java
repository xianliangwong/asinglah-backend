package com.asinglah.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asinglah.backend.Entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense,Long>{

}
