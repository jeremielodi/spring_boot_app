package com.digipay.app.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digipay.app.models.Account;
import com.digipay.app.repository.AccountRepository;
import com.digipay.app.repository.TransactionRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;

    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    @GetMapping("/balance/{acountId}")
    public ResponseEntity<?> getBalanceProcess(@PathVariable String acountId) {
        try {
            Long selectedAccountId = Long.valueOf(acountId);
            Optional<Account> account = accountRepository.findById(selectedAccountId);
            if (!account.isPresent()) {
                HashMap<String, Object> errorMap = new HashMap<>();
                errorMap.put("status", 0);
                errorMap.put("code", 400);
                errorMap.put("message", "ACCOUNT_NOT_FOUND");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
            }
            Account currentAccount = account.get();
            Double currenctBalance = transactionRepository.accountBalance(selectedAccountId);
            HashMap<String, Object> map = new HashMap<>();
            map.put("accountId", currentAccount.getId());
            map.put("accountNumber", currentAccount.getAccountNumber());
            map.put("accountTitle", currentAccount.getAccountTitle());
            map.put("balance", currenctBalance);
            map.put("currencyId", currentAccount.getCurrency().getId());
            map.put("currencySymbol", currentAccount.getCurrency().getSymbol());
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    @GetMapping("/detail/{acountNumber}")
    public ResponseEntity<?> findByNumber(@PathVariable String acountNumber) {
        try {

            Optional<Account> account = accountRepository.findByAccountNumber(acountNumber);

            if (!account.isPresent()) {
                HashMap<String, Object> errorMap = new HashMap<>();
                errorMap.put("status", 0);
                errorMap.put("code", 400);
                errorMap.put("message", "ACCOUNT_NOT_FOUND");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
            }

            Account currentAccount = account.get();
            HashMap<String, Object> map = new HashMap<>();
            map.put("accountId", currentAccount.getId());
            map.put("accountNumber", currentAccount.getAccountNumber());
            map.put("accountTitle", currentAccount.getAccountTitle());
            map.put("currencyId", currentAccount.getCurrency().getId());
            map.put("currencySymbol", currentAccount.getCurrency().getSymbol());
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> getAccounts() {
        try {

            List<Account> accounts = accountRepository.findAll();
            List<HashMap<String, Object>> ListMap = new ArrayList<>();
            for (int i = 0; i < accounts.size(); i++) {
                Account currentAccount = accounts.get(i);
                Double currenctBalance = transactionRepository.accountBalance(currentAccount.getId());
                HashMap<String, Object> map = new HashMap<>();
                map.put("accountId", currentAccount.getId());
                map.put("accountNumber", currentAccount.getAccountNumber());
                map.put("accountTitle", currentAccount.getAccountTitle());
                map.put("balance", currenctBalance);
                map.put("currencyId", currentAccount.getCurrency().getId());
                map.put("currencySymbol", currentAccount.getCurrency().getSymbol());
                ListMap.add(map);
            }
            return ResponseEntity.ok(ListMap);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            // TODO: handle exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list/{userId}")
    public ResponseEntity<?> userAccounts(@PathVariable String userId) {
        try {
            Long _userId = Long.valueOf(userId);
            List<Account> accounts = accountRepository.findByUserId(_userId);
            List<HashMap<String, Object>> ListMap = new ArrayList<>();
            for (int i = 0; i < accounts.size(); i++) {
                Account currentAccount = accounts.get(i);
                Double currenctBalance = transactionRepository.accountBalance(currentAccount.getId());
                HashMap<String, Object> map = new HashMap<>();
                map.put("accountId", currentAccount.getId());
                map.put("accountNumber", currentAccount.getAccountNumber());
                map.put("accountTitle", currentAccount.getAccountTitle());
                map.put("balance", currenctBalance);
                map.put("currencyId", currentAccount.getCurrency().getId());
                map.put("currencySymbol", currentAccount.getCurrency().getSymbol());
                ListMap.add(map);
            }
            return ResponseEntity.ok(ListMap);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            // TODO: handle exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}