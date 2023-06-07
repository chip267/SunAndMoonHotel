package com.example.sunmoon.models;

public class Booking {
    public String bookingID;
    public String checkinDate;
    public String checkoutDate;
    //public String bookingTime;
    public String bookingType; //Day or Hour
    public String roomID;
    public double total;
    public String status;

    public Booking(String bookingID, String checkinDate, String checkoutDate,
                   String bookingType, String roomID, double total, String status) {
        this.bookingID = bookingID;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.bookingType = bookingType;
        this.roomID = roomID;
        this.total = total;
        this.status = status;
    }
}

