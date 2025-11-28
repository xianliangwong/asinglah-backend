package com.asinglah.backend.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="expense_split")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Expense_split {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expenseSplit_id") // DB column name
    private long id;

    @ManyToOne
    @JoinColumn(name ="expense_id",nullable = false)
    private Expense expenseId ;


    @ManyToOne
    @JoinColumn(name ="participant_user_id",nullable = false)
    private User partipcantUser;

    @Column(name = "totalAmount_owed", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmountOwed;
    // DECIMAL in SQL Server maps well to BigDecimal in Java
    // precision = total digits, scale = digits after decimal

    @Column(name = "remainingAmount_owed", precision = 15, scale = 2, nullable = false)
    private BigDecimal remainingAmountOwed;
    // DECIMAL in SQL Server maps well to BigDecimal in Java
    // precision = total digits, scale = digits after decimal

    // BOOLEAN column
    @Column(name = "is_settled", nullable = false)
    private boolean isSettled;

    // TIMESTAMP column
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // One Expense → Many ExpenseSplits
    @OneToMany(mappedBy = "expenseSplitId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseTransaction_request> trans_req = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.of(1900, 1, 1, 0, 0);
    }


    


}
