package com.digipay.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.digipay.app.models.Transactions;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    Optional<Transactions> findByReferenceNumber(String referenceNumber);

    @Query(value = "SELECT (x.deposits - x.withdrawals) AS balance " +
            "FROM (" +
            "SELECT " +

            " SUM(IF(is_exit=0, amount, 0)) as deposits, " +
            " SUM(IF(is_exit=1, amount, 0)) as withdrawals " +

            "FROM transactions WHERE account_id = 1) AS x ", nativeQuery = true)
    public Double accountBalance(Long accountId);

    @Query(value = "SELECT * FROM transactions WHERE account_id=:accountId", nativeQuery = true)
    List<Transactions> findByAccountId(Long accountId);

    @Query(value = "SELECT * FROM transactions", nativeQuery = true)
    List<Transactions> findAll();

}