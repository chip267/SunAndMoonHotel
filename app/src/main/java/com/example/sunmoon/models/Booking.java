package com.example.sunmoon.models;

public class Booking {
    public String bookingID;
    public String checkinDate;
    public String checkinHour;
    public String checkoutDate;
    public String checkoutHour;
    public String bookingType;
    public String roomID;
    public double total;
    public String status;
    public String gIDCard;
    public Booking(){
        this.bookingID = "";
        this.checkinDate = "";
        this.checkoutDate = "";
        this.checkinHour = "";
        this.checkoutHour = "";
        this.bookingType = "";
        this.roomID = "";
        this.total = 0;
        this.status = "";
        this.gIDCard="";
    }
    public Booking (String bookingID, String checkinDate, String checkoutDate, String checkinHour, String checkoutHour,
                    String bookingType, String roomID, double total, String status, String gIDCard){
        this.bookingID = bookingID;
        this.roomID = roomID;
        this.gIDCard = gIDCard;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.checkinHour = checkinHour;
        this.checkoutHour = checkoutHour;
        this.bookingType=bookingType;
        this.total=total;
        this.status=status;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }
    public String getCheckinHour() {
        return checkinHour;
    }

    public String getCheckoutHour() {
        return checkoutHour;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getGIDCard() {
        return gIDCard;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }
    public void setCheckinHour(String checkinHour) {
        this.checkinHour = checkinHour;
    }

    public void setCheckoutHour(String checkoutHour) {
        this.checkoutHour = checkoutHour;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    public void setGIDCard(String gIDCard) {
        this.gIDCard = gIDCard;
    }
}

