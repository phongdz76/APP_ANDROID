package com.example.doannam2;

public class Cartdata {
    private String dataTitle;
    private int datalang;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private String dataimage;
    private  String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Cartdata(String dataTitle,  int datalang, String dataimage) {
        this.dataTitle = dataTitle;

        this.datalang = datalang;
        this.dataimage = dataimage;
    }
    public  Cartdata(){

    }
    public String getDataTitle() {
        return dataTitle;
    }

    public int getDatalang() {
        return datalang;
    }

    public String getDataimage() {
        return dataimage;
    }
}
