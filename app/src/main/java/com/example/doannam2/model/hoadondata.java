package com.example.doannam2.model;

public class hoadondata {
    private String email;
    private String name;

    public hoadondata(String email, String name) {
        this.email = email;
        this.name = name;
    }

    private String dơnhang;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDơnhang() {
        return dơnhang;
    }

    public void setDơnhang(String dơnhang) {
        this.dơnhang = dơnhang;
    }
}
