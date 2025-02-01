package com.khanh.expensemanagement.m_name.view;

public class MName {

    private Integer id;

    private String nameIdentCd;

    private String nameCd;

    private String nameIdentName;

    private String nameIdentNote;

    private Integer nameDisplaySeq;

    private String nameSs;

    private String nameRk;

    private String drawableIconUrl;

    private String insDttm;

    private String updDttm;

    public MName() {
    }

    public MName(Integer id, String nameIdentCd, String nameCd, String nameIdentName, String nameIdentNote, Integer nameDisplaySeq, String nameSs, String nameRk, String drawableIconUrl, String insDttm, String updDttm) {
        this.id = id;
        this.nameIdentCd = nameIdentCd;
        this.nameCd = nameCd;
        this.nameIdentName = nameIdentName;
        this.nameIdentNote = nameIdentNote;
        this.nameDisplaySeq = nameDisplaySeq;
        this.nameSs = nameSs;
        this.nameRk = nameRk;
        this.drawableIconUrl = drawableIconUrl;
        this.insDttm = insDttm;
        this.updDttm = updDttm;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDrawableIconUrl() {
        return drawableIconUrl;
    }

    public void setDrawableIconUrl(String drawableIconUrl) {
        this.drawableIconUrl = drawableIconUrl;
    }

    public String getNameIdentCd() {
        return nameIdentCd;
    }

    public void setNameIdentCd(String nameIdentCd) {
        this.nameIdentCd = nameIdentCd;
    }

    public String getNameCd() {
        return nameCd;
    }

    public void setNameCd(String nameCd) {
        this.nameCd = nameCd;
    }

    public String getNameIdentName() {
        return nameIdentName;
    }

    public void setNameIdentName(String nameIdentName) {
        this.nameIdentName = nameIdentName;
    }

    public String getNameIdentNote() {
        return nameIdentNote;
    }

    public void setNameIdentNote(String nameIdentNote) {
        this.nameIdentNote = nameIdentNote;
    }

    public Integer getNameDisplaySeq() {
        return nameDisplaySeq;
    }

    public void setNameDisplaySeq(Integer nameDisplaySeq) {
        this.nameDisplaySeq = nameDisplaySeq;
    }

    public String getNameSs() {
        return nameSs;
    }

    public void setNameSs(String nameSs) {
        this.nameSs = nameSs;
    }

    public String getNameRk() {
        return nameRk;
    }

    public void setNameRk(String nameRk) {
        this.nameRk = nameRk;
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
