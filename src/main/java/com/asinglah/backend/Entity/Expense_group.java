package com.asinglah.backend.Entity;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="expense_group")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Expense_group {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expenseGroup_id") // DB column name
    private long expenseGroupId;

    @Column(name="group_name", length =255, nullable = false)
    private String groupName;

    @ManyToOne
    @JoinColumn(name ="groupOwner_id",nullable = false)
    private User groupOwnerId;

     // TIMESTAMP column
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

     // Optional members: list can be empty at first
    @OneToMany(mappedBy = "expense_group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<group_member> members = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


    
}
