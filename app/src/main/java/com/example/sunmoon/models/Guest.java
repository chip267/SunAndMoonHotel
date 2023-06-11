package com.example.sunmoon.models;

public class Guest {
    public String gIDCard;
    public String gName;
    public String gPhone;
    public String gBirthday;
    public String gender;
    public boolean gAvail;


    public Guest(String gIDCard, String gName,String gender, String gPhone, String gBirthday, boolean gAvail) {
        this.gIDCard = gIDCard;
        this.gName = gName;
        this.gender = gender;
        this.gPhone = gPhone;
        this.gBirthday = gBirthday;
        this.gAvail = gAvail;
    }
    public Guest(){
        this.gIDCard = "";
        this.gName = "";
        this.gender = "";
        this.gPhone = "";
        this.gBirthday = "";
        this.gAvail = false;
    }

    public Guest(String gName, String gBirthday, String gIDCard, String gPhone) {
        this.gIDCard = gIDCard;
        this.gName = gName;
        this.gPhone = gPhone;
        this.gBirthday = gBirthday;
    }

    public void setgAvail(boolean gAvail) {
        this.gAvail = gAvail;
    }

    public String getgBirthday() {
        return gBirthday;
    }

    public void setgBirthday(String gBirthday) {
        this.gBirthday = gBirthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getgName() {
        return gName;
    }

    public String getgIDCard() {
        return gIDCard;
    }

    public void setgIDCard(String gIDCard) {
        this.gIDCard = gIDCard;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getgPhone() {
        return gPhone;
    }

    public void setgPhone(String gPhone) {
        this.gPhone = gPhone;
    }
}
