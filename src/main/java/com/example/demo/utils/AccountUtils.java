package com.example.demo.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE="001";
    public static final String ACCOUNT_EXISTS_MESSAGE="This User already has an account Created!!";
    public static final String ACCOUNT_CREATION_SUCCESS ="002";
    public static final String ACCOUNT_CREATION_MESSAGE ="Account has been Successfully Created.";
    public static final String ACCOUNT_NOT_EXIST_CODE="003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE="User With the Provided Account Number does not Exists";
    public static final String ACCOUNT_FOUND_CODE="004";
    public static final String ACCOUNT_FOUND_SUCCESS="User Account Found...";

    public static final String ACCOUNT_CREDITED_SUCCESS ="005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE =" The Amount Is Successfully Credited into the User Account";

    public  static  final String INSUFFICIENT_BALANCE_CODE="006";
    public  static  final String INSUFFICIENT_BALANCE_MESSAGE="Insufficient Balance";

    public  static  final String ACCOUNT_DEBITED_SUCCESS_CODE="007";
    public static  final String ACCOUNT_DEBITED_MESSAGE="The Amount is Successfully Debited From the Account";

   /* public static  final String DEBIT_ACCOUNT_NOT_EXIST_CODE="008";
    public static  final String DEBIT_ACCOUNT_NOT_EXIST_MESSAGE="The Debit Account is not exists.";
*/
    public  static  final String TRANSFER_SUCCESSFUL_CODE="008";
    public  static  final String TRANSFER_SUCCESSFUL_MESSAGE=" Transfer is Successful--->";

    public static String generateAccountNumber(){
        Year currentYear= Year.now();
        int min = 100000;
        int max =999999;
        //generate a random number between min and max
        int randNumber = (int)Math.floor(Math.random()*(max-min+1)+min);
        //convert the current year and randomNumber to string,them concatenate
        String year =String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);
        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(randomNumber).toString();

    }
}
