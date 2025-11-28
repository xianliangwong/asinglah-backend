package com.asinglah.backend.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="expenseTransaction_request")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseTransaction_request {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id") // DB column name
    private long transactionId;

    @ManyToOne
    @JoinColumn(name ="expenseSplit_id",nullable = false)
    private Expense_split expenseSplitId;

    @ManyToOne
    @JoinColumn(name="payer_id",nullable  =false)
    private User payerId;

    @ManyToOne
    @JoinColumn(name="payee_id",nullable =false)
    private User payeeId;

    @Column(name = "amount_paid", precision = 15, scale = 2, nullable = false)
    private BigDecimal amountPaid;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.of(1900, 1, 1, 0, 0);
    }

    @ManyToOne
    @JoinColumn(name="statusID",nullable=false)
    private StatusCode statusID;




}
