package com.khanh.expensemanagement.naiji;

class ExcelDownloadData {

    private  Integer transactionId;
    private  Integer amount;
    private  String note;
    private  Integer categoryId;
    private  String categoryTitle;
    private  Integer sourceId;
    private String sourceName;
    private String transactionDt;
    private String insDttm;
    private String updDttm;

    public ExcelDownloadData() {
    }

    public ExcelDownloadData(Integer transactionId, Integer amount, String note, Integer categoryId, String categoryTitle, Integer sourceId, String sourceName, String transactionDt, String insDttm, String updDttm) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.note = note;
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.transactionDt = transactionDt;
        this.insDttm = insDttm;
        this.updDttm = updDttm;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getTransactionDt() {
        return transactionDt;
    }

    public void setTransactionDt(String transactionDt) {
        this.transactionDt = transactionDt;
    }

    public String getInsDttm() {
        return insDttm;
    }

    public void setInsDttm(String insDttm) {
        this.insDttm = insDttm;
    }

    public String getUpdDttm() {
        return updDttm;
    }

    public void setUpdDttm(String updDttm) {
        this.updDttm = updDttm;
    }
}
