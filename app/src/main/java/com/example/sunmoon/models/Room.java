package com.example.sunmoon.models;

public class Room {
    public String roomID;
    public String roomStatus;
    public String roomType;
    public double pricebyHour;
    public double pricebyDay;
    public boolean rAvail;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPricebyHour() {
        return pricebyHour;
    }

    public void setPricebyHour(double pricebyHour) {
        this.pricebyHour = pricebyHour;
    }

    public double getPricebyDay() {
        return pricebyDay;
    }

    public void setPricebyDay(double pricebyDay) {
        this.pricebyDay = pricebyDay;
    }

    public boolean isrAvail() {
        return rAvail;
    }

    public void setrAvail(boolean rAvail) {
        this.rAvail = rAvail;
    }

    public Room(String roomID, String roomStatus, String roomType, double pricebyHour,
                double pricebyDay, boolean rAvail) {
        this.roomID = roomID;
        this.roomStatus = roomStatus;
        this.roomType = roomType;
        this.pricebyHour = pricebyHour;
        this.pricebyDay = pricebyDay;
        this.rAvail = rAvail;
    }
}
