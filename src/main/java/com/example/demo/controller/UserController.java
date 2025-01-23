package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name="User Account Management APIs")

public class UserController {
    @Autowired
    UserService userService;
    @Operation(
            summary = "Create New User Account",
            description = "Creating a new user and assigning an account ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 Created..."

    )

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
      return  userService.createAccount(userRequest);
    }
    @Operation(
            summary = "Balance Enquiry",
            description = "Given an account Number,Check how much the user have in his account..."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 Created..."

    )
    @GetMapping("balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request){
        return userService.balanceEnquiry(request);
    }
    @GetMapping("nameEnquiry")
    public  String nameEnquiry(@RequestBody EnquiryRequest request){
        return  userService.nameEnquiry(request);
    }
    @PostMapping("credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request){
        return  userService.creditAccount(request);
    }
    @PostMapping("debit")
    public  BankResponse debitAccount(@RequestBody CreditDebitRequest request ){
        return  userService.DebitAmount(request);
    }
    @PostMapping("transfer")
    public  BankResponse transfer(@RequestBody TransferRequest request){
        return  userService.transfer(request);
    }
}
