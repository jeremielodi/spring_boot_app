package com.digipay.app.payload.request;

import javax.validation.constraints.NotBlank;

public class WithdrawalRequest {

    @NotBlank
    private String accountNumber;
    @NotBlank
    private String amount;

    @NotBlank
    private String agentNumber;

    @NotBlank
    private String description;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAgentNumber() {
        return agentNumber;
    }

    public void setAgentNumber(String agentNumber) {
        this.agentNumber = agentNumber;
    }

}
