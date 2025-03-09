package com.khanh.expensemanagement.budget_mainte;

class Category {

    private Integer categoryId;
    private String categoryName;
    private String drawableIconUrl;

    public Category(Integer categoryId, String categoryName, String drawableIconUrl) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.drawableIconUrl = drawableIconUrl;
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

    public String getDrawableIconUrl() {
        return drawableIconUrl;
    }

    public void setDrawableIconUrl(String drawableIconUrl) {
        this.drawableIconUrl = drawableIconUrl;
    }
}
