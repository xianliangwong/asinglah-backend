package com.asinglah.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.asinglah.backend.Entity.ExpenseTransaction_request;



public interface ExpenseTranReqRepository extends JpaRepository<ExpenseTransaction_request,Long> {

    
}