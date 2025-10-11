package com.asinglah.backend.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // DB column name
    private long id;

    @Column(name="full_name",length =100,nullable = false)
    private String fullName;

    @Column(name="email_address",length=200,nullable = false)
    private String emailAddress;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    // TIMESTAMP in SQL Server maps to LocalDateTime in Java

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    // TIMESTAMP in SQL Server maps to LocalDateTime in Java

    @PreUpdate
    protected void onUpdate() 
    {
    this.updatedAt = LocalDateTime.now();}

    @Column(name = "password_hash", nullable = false, length = 255)
    private String password;
    

}
