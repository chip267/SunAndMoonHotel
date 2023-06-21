package com.example.sunmoon.models;

public class Booking {
    public String bookingID;
    public String checkinDate;
    public String checkinHour;
    public String checkoutDate;
    public String checkoutHour;
    public String bookingType;
    public String rid;
    public int total;
    public int surcharge;
    public int totalBill;
    public String status;
    public String gid;
    public String details;

    public Booking(){
        this.bookingID = "";
        this.checkinDate = "";
        this.checkoutDate = "";
        this.checkinHour = "";
        this.checkoutHour = "";
        this.bookingType = "";
        this.rid = "";
        this.total = 0;
        this.status = "";
        this.gid="";
        this.surcharge=0;
        this.totalBill=0;
        this.details="";
    }
    public Booking (String bookingID, String checkinDate, String checkoutDate, String checkinHour, String checkoutHour,
                    String bookingType, String rid, int total, String status, String gid, int surcharge, int totalBill, String details){
        this.bookingID = bookingID;
        this.rid = rid;
        this.gid = gid;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.checkinHour = checkinHour;
        this.checkoutHour = checkoutHour;
        this.bookingType=bookingType;
        this.total=total;
        this.status=status;
        this.surcharge=surcharge;
        this.totalBill=totalBill;
        this.details=details;
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

    public String getRid() {
        return rid;
    }

    public String getGid() {
        return gid;
    }
    public int getTotal() { return total; }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getSurcharge() {
        return surcharge;
    }

    public int getTotalBill() {
        return totalBill;
    }

    public String getBookingID() {
        return bookingID;
    }
    public String getBookingType() {
        return bookingType;
    }
    public String getStatus() {
        return status;
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

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setSurcharge(int surcharge) {
        this.surcharge = surcharge;
    }

    public void setTotalBill(int totalBill) {
        this.totalBill = totalBill;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }
}

