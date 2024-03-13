package com.example.doannam2.model;

public class Cartdata {
    private String dataTitle;
    private int dataPrice;
    private String dataimage;
    private int quantity; // Số lượng sản phẩm trong giỏ hàng
    private String key;



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public Cartdata(){
        this.quantity =1;
    }
    public Cartdata(String dataTitle, int dataPrice, String dataimage) {
        this.dataTitle = dataTitle;
        this.dataPrice = dataPrice;
        this.dataimage = dataimage;
    }


    public String getDataTitle() {
        return dataTitle;
    }

    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public int getDataPrice() {
        return dataPrice;
    }

    public void setDataPrice(int dataPrice) {
        this.dataPrice = dataPrice;
    }

    public String getDataimage() {
        return dataimage;
    }

    public void setDataimage(String dataimage) {
        this.dataimage = dataimage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int calulateTotalPrice(){
        return this.quantity * this.dataPrice;
    }
}
