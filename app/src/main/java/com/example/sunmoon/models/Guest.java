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
}
