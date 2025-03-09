package com.khanh.expensemanagement.budget_mainte;

class Budget {

    private Integer limitAmount;
    private Integer categoryId;

    public Integer getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(Integer limitAmount) {
        this.limitAmount = limitAmount;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Budget() {
    }
}
