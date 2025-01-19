package com.khanh.expensemanagement.home;

import java.util.ArrayList;

public class TransactionHistory {

    private  Integer transactionId;
    private  String transactionTitle;
    private  String categoryTitle;
    private  String transactionUp;

    public TransactionHistory(Integer transactionId, String transactionTitle, String categoryTitle, String transactionUp) {
        this.transactionId = transactionId;
        this.transactionTitle = transactionTitle;
        this.categoryTitle = categoryTitle;
        this.transactionUp = transactionUp;
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

    public String getTransactionUp() {
        return transactionUp;
    }

    public void setTransactionUp(String transactionUp) {
        this.transactionUp = transactionUp;
    }
}
