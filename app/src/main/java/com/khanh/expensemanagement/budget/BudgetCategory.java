package com.khanh.expensemanagement.budget;

import java.math.BigInteger;

class BudgetCategory {

    private Integer budgetId;
    private Integer categoryId;
    private String categoryName;
    private BigInteger spentAmount;
    private BigInteger limitAmount;
    private String drawableIconUrl;

    public BudgetCategory(Integer budgetId, Integer categoryId, String categoryName, BigInteger spentAmount, BigInteger limitAmount, String drawableIconUrl) {
        this.budgetId = budgetId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.spentAmount = spentAmount;
        this.limitAmount = limitAmount;
        this.drawableIconUrl = drawableIconUrl;
    }

    public BudgetCategory() {
    }

    public Integer getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Integer budgetId) {
        this.budgetId = budgetId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigInteger getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigInteger spentAmount) {
        this.spentAmount = spentAmount;
    }

    public BigInteger getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigInteger limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getDrawableIconUrl() {
        return drawableIconUrl;
    }

    public void setDrawableIconUrl(String drawableIconUrl) {
        this.drawableIconUrl = drawableIconUrl;
    }
}
