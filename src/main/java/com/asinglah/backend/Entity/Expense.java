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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.JoinColumn;




@Entity
@Table(name="expense")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id") // DB column name
    private long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Expense_group expense_group;

    @OneToOne
    @JoinColumn(name ="user_id",nullable = false)
    private User userId;

    // One Expense → Many ExpenseSplits
    @OneToMany(mappedBy = "expenseId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense_split> splits = new ArrayList<>();


    @Column(name="description",length = 600)
    private String description;

    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;
    // DECIMAL in SQL Server maps well to BigDecimal in Java
    // precision = total digits, scale = digits after decimal

    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode;
    // CHAR(3) in SQL Server maps to String with length = 3

     @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    // TIMESTAMP in SQL Server maps to LocalDateTime in Java

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

     // Helper method to add a split
    public void addSplit(Expense_split split) {
        splits.add(split);
        split.setExpenseId(this);
    }



    


}
