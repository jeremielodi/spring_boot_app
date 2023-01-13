package com.digipay.app.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Transfert", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "fromTransactionId", "toTransactionId" }),
})
public class Transfert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromTransactionId")
    @NotNull
    private Transactions fromTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toTransactionId")
    @NotNull
    private Transactions toTransaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transactions getFromTransaction() {
        return fromTransaction;
    }

    public void setFromTransaction(Transactions fromTransaction) {
        this.fromTransaction = fromTransaction;
    }

    public Transactions getToTransaction() {
        return toTransaction;
    }

    public void setToTransaction(Transactions toTransaction) {
        this.toTransaction = toTransaction;
    }

}
