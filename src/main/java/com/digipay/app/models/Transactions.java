package com.digipay.app.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "transactions", uniqueConstraints = {
        @UniqueConstraint(columnNames = "referenceNumber"),
}, indexes = {
        @Index(name = "accountId", columnList = "accountId", unique = false)
})
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 40)
    private String referenceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    @NotNull
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private ETransactionType transactionType;

    @NotNull
    @Column(length = 250)
    private String description;

    @NotNull
    private Double gain;

    @NotNull
    private int isExit;

    private int fromTransfert = 0;

    @NotNull
    @Column(precision = 19, scale = 4)
    private Double commission = 0.0;

    @NotNull
    private java.sql.Timestamp createdAt;

    public Long getId() {
        return id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public User getUser() {
        return user;
    }

    public Double getAmount() {
        return amount;
    }

    public ETransactionType getTransactionType() {
        return transactionType;
    }

    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setTransactionType(ETransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getIsExit() {
        return isExit;
    }

    public void setIsExit(int isExit) {
        this.isExit = isExit;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getFromTransfert() {
        return fromTransfert;
    }

    public void setFromTransfert(int fromTransfert) {
        this.fromTransfert = fromTransfert;
    }

    public Double getGain() {
        return gain;
    }

    public void setGain(Double gain) {
        this.gain = gain;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

}
