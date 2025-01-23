package com.example.demo.service;

import com.example.demo.dto.TransactionDto;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionImpl implements TransactionServie{
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void savedTransaction (TransactionDto transactionDto){
        Transaction transaction= Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("Success")
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction saved Successfully");

    }
}
