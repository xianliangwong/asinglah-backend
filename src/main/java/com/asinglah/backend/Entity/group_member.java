package com.asinglah.backend.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="group_member")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class group_member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groupMember_id") // DB column name
    private long id;

    @ManyToOne
    @JoinColumn(name ="expenseGroup_id",nullable = false)
    private Expense_group expense_group;

    @OneToOne
    @JoinColumn(name ="user_id",nullable = false)
    private User user_id;

    @OneToOne
    @JoinColumn(name="statusID",nullable=false)
    private StatusCode statusID;

    @Column(name = "left_at", nullable = false)
    private LocalDateTime left_at; 

     // TIMESTAMP column
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.left_at = LocalDateTime.of(1900, 1, 1, 0, 0);
    }



}
