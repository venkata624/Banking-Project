package com.example.demo.service;

import com.example.demo.dto.TransactionDto;
import com.example.demo.entity.Transaction;
import org.springframework.stereotype.Service;


public interface TransactionServie {
    void savedTransaction(TransactionDto transactiondro);

}
