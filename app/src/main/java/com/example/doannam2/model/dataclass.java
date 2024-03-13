package com.example.doannam2.model;

public class dataclass {
    private String dataTitle;
    private String dataDesc;
    private int datalang;
    private String dataimage;
    private  String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public dataclass(String dataTitle, String dataDesc, int datalang, String dataimage) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.datalang = datalang;
        this.dataimage = dataimage;
    }
    public  dataclass(){

    }
    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public int getDatalang() {
        return datalang;
    }

    public String getDataimage() {
        return dataimage;
    }
}
