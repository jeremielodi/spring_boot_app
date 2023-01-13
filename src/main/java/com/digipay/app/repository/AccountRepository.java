package com.digipay.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digipay.app.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByUserId(Long userId);
}
