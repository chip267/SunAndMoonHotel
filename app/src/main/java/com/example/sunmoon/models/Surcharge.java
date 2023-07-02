package com.example.sunmoon.models;

public class Surcharge {
    String detail;
    String price;

    public  Surcharge(){

    }

    public Surcharge(String detail, String price) {
        this.detail = detail;
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public String getPrice() {
        return price;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
