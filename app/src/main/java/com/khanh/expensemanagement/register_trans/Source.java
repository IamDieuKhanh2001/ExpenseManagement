package com.khanh.expensemanagement.register_trans;

public class Source {

    private Integer id;
    private String nameSs;
    private String image;

    public Source(Integer id, String nameSs, String image) {
        this.id = id;
        this.nameSs = nameSs;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameSs() {
        return nameSs;
    }

    public void setNameSs(String nameSs) {
        this.nameSs = nameSs;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
