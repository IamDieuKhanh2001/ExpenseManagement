package com.khanh.expensemanagement.home;

import java.util.ArrayList;

public class TransactionHistory {

    private  Integer transactionId;
    private  String transactionTitle;
    private  String categoryTitle;
    private  Integer amount;

    public TransactionHistory(Integer transactionId, String transactionTitle, String categoryTitle, Integer amount) {
        this.transactionId = transactionId;
        this.transactionTitle = transactionTitle;
        this.categoryTitle = categoryTitle;
        this.amount = amount;
    }

    public TransactionHistory() {
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionTitle() {
        return transactionTitle;
    }

    public void setTransactionTitle(String transactionTitle) {
        this.transactionTitle = transactionTitle;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
