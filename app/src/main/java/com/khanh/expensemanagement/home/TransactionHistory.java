package com.khanh.expensemanagement.home;

import java.util.ArrayList;

public class TransactionHistory {

    private  Integer transactionId;
    private  String note;
    private  String categoryTitle;
    private  Integer amount;
    private String sourceName;
    private String date;
    private String updDttm;

    public TransactionHistory(Integer transactionId, String note, String categoryTitle, Integer amount, String sourceName, String date, String updDttm) {
        this.transactionId = transactionId;
        this.note = note;
        this.categoryTitle = categoryTitle;
        this.amount = amount;
        this.sourceName = sourceName;
        this.date = date;
        this.updDttm = updDttm;
    }

    public TransactionHistory() {
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUpdDttm() {
        return updDttm;
    }

    public void setUpdDttm(String updDttm) {
        this.updDttm = updDttm;
    }
}
