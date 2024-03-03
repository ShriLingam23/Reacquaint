package com.example.springbatchsample.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String number;
    private Double amount;
    private Double discount;
    private Double finalAmount;
    private String location;

}