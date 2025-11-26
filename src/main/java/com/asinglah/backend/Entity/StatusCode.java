package com.asinglah.backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="StatusCode")
public class StatusCode {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusID") // DB column name
    private long statusID;

    @Column(name="statusDesc", length =300, nullable = false)
    private String statusDesc;


}
