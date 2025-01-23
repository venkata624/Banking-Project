package com.example.demo.service;

import com.example.demo.dto.*;

public interface UserService  {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse DebitAmount(CreditDebitRequest request);
    BankResponse transfer(TransferRequest request);

}
