package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class UserServiceimpl implements  UserService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionServie transactionServie;

    @Override
    public BankResponse createAccount(UserRequest userRequests) {
        /*
        Creating an account -saving a new user into the database...
        Check if the user already has an account
         */
        if(userRepository.existsByEmail(userRequests.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responsemsg(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                    .accountInfo(null)
                    .build();

        }
        User newUser = User.builder()
                .fristName(userRequests.getFristName())
                .lastName(userRequests.getLastName())
                .otherName(userRequests.getOtherName())
                .gender(userRequests.getGender())
                .address(userRequests.getAddress())
                .stateOfOrigin(userRequests.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequests.getEmail())
                .phoneNumber(userRequests.getPhoneNumber())
                .alternativePhoneNumber(userRequests.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();
        User savedUser= userRepository.save(newUser);
        //Send email Alerts
        EmailDetails emailDetails= EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congrats! Your Account is Successfully Created.....\n Your Account Details;\n" +
                        "Account Name:" +savedUser.getFristName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName()+"\n Account Number:" + savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responsemsg(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFristName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        //Check if the provided account  exists in the db
        boolean isAccountExist= userRepository.existsByAccountNumber(request.getAccountNumber());
       if(!isAccountExist){
           return BankResponse.builder()
                   .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                   .responsemsg(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                   .accountInfo(null)
                   .build();
       }
        User founderUser=userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responsemsg(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(founderUser.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .accountName(founderUser.getFristName()+" "+founderUser.getLastName()+" "+founderUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist= userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User founderUser=userRepository.findByAccountNumber(request.getAccountNumber());
        return founderUser.getFristName()+" "+founderUser.getLastName()+" "+founderUser.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        // Checking if the Account Exist..
        boolean isAccountExist= userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responsemsg(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit= userRepository.findByAccountNumber(request.getAccountNumber());
         userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
         userRepository.save(userToCredit);
         TransactionDto transactionDto= TransactionDto.builder()
                 .accountNumber(userToCredit.getAccountNumber())
                 .transactionType("Credit")
                 .amount(request.getAmount())
                 .build();

         transactionServie.savedTransaction(transactionDto);

        return  BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responsemsg(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFristName()+ " "+ userToCredit.getLastName()+userToCredit.getOtherName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(userToCredit.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse DebitAmount(CreditDebitRequest request) {
        // check if the account exists
        //check if the amount you intend to withdraw is not more than Current account balance
        boolean isAccountExist= userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responsemsg(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
       User userToDebit= userRepository.findByAccountNumber(request.getAccountNumber());
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigDecimal debitAmount= request.getAmount();
        if(availableBalance.intValue()< debitAmount.intValue()){
            return  BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responsemsg(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        else{
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);
            TransactionDto transactionDto= TransactionDto.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("Credit")
                    .amount(request.getAmount())
                    .build();

            transactionServie.savedTransaction(transactionDto);
            return  BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                    .responsemsg(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDebit.getFristName()+ " "+ userToDebit.getLastName()+userToDebit.getOtherName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .accountNumber(userToDebit.getAccountNumber())
                            .build())
                    .build();
        }
    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        //get the account to debit(check if it exists)
        //check if the amount im debiting is not more than the Current balance
        //debit the account
        //get the account to credit
        //credit the amount
        boolean isDestinationAccountExists=userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if(!isDestinationAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responsemsg(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User sourceAccount= userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if(request.getAmount().compareTo(sourceAccount.getAccountBalance())>0){
            return  BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responsemsg(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(request.getAmount()));
        String sourceUsername =sourceAccount.getFristName()+" "+ sourceAccount.getLastName()+" " +sourceAccount.getOtherName();
        userRepository.save(sourceAccount);
        EmailDetails debitAlert= EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccount.getEmail())
                .messageBody("The Sum of "+request.getAmount()+"has been deducted from your account! Your Current balance is"+sourceAccount.getAccountBalance())
                .build();
        //emailService.sendEmailAlert(debitAlert);

        User destinationAccountUser= userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        /*String recipientName=destinationAccountUser.getFristName()+" "+destinationAccountUser.getLastName()+" "+destinationAccountUser.getOtherName();*/
        userRepository.save(destinationAccountUser);
        EmailDetails creditAlert= EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(sourceAccount.getEmail())
                .messageBody("The Sum of "+request.getAmount()+"has been sent from your "+sourceUsername+" Your Current balance is"+sourceAccount.getAccountBalance())
                .build();
        //emailService.sendEmailAlert(debitAlert);
        TransactionDto transactionDto= TransactionDto.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType("Credit")
                .amount(request.getAmount())
                .build();

        transactionServie.savedTransaction(transactionDto);

        return
                BankResponse .builder()
                        .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                        .responsemsg(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                        .accountInfo(null)
                        .build();


    }

    //balance Enquiry,name Enquiry,credit, debt,transfer..
}
