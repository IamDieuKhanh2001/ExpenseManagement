package com.khanh.expensemanagement.naiji;

import java.math.BigInteger;

public class CategoryExpense {

    private Integer id;
    private String categoryName;
    private BigInteger totalSpent;

    public CategoryExpense(Integer id, String categoryName, BigInteger totalSpent) {
        this.id = id;
        this.categoryName = categoryName;
        this.totalSpent = totalSpent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigInteger getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigInteger totalSpent) {
        this.totalSpent = totalSpent;
    }
}
