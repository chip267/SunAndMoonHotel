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

    private  String guestID;
    private String guestName;
    private String guestPhone;

    public Booking (String bookingID, String checkinDate, String checkoutDate, String roomID, String guestName, String guestPhone){
        this.bookingID = bookingID;
        this.roomID = roomID;
        this.guestName = guestName;
        this.guestPhone = guestPhone;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getGuestID() {
        return guestID;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

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
    public Booking(){
        this.bookingID = "";
        this.checkinDate = "";
        this.checkoutDate = "";
        this.bookingType = "";
        this.roomID = "";
        this.total = 0;
        this.status = "";
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

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

