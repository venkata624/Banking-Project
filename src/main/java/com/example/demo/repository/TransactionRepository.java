package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.ref.SoftReference;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
