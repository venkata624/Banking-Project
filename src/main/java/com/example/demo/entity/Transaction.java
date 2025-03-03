package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name ="transctions")

public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String  transactionId;
    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;

}
