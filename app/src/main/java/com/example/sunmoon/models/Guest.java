package com.example.sunmoon.models;

public class Guest {
    public String gIDCard;
    public String gName;
    public String gPhone;
    public String gBirthday;
    public boolean gAvail;


    public Guest(String gIDCard, String gName, String gPhone, String gBirthday, boolean gAvail) {
        this.gIDCard = gIDCard;
        this.gName = gName;
        this.gPhone = gPhone;
        this.gBirthday = gBirthday;
        this.gAvail = gAvail;
    }
}
