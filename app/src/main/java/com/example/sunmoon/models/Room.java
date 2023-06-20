package com.example.sunmoon.models;

import java.util.Comparator;

public class Room {
    public String roomID;
    public String roomType;
    public double pricebyHour;
    public double pricebyDay;
    public int rAvail;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
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

    public int isrAvail() {
        return rAvail;
    }

    public void setrAvail(int rAvail) {
        this.rAvail = rAvail;
    }
    public Room()
    {
    }

    public Room(String roomID, String roomType, double pricebyHour, double pricebyDay, int rAvail) {
        this.roomID = roomID;
        this.roomType = roomType;
        this.pricebyHour = pricebyHour;
        this.pricebyDay = pricebyDay;
        this.rAvail = rAvail;
    }

    public static Comparator<Room> AscendingPriceComparator = new Comparator<Room>() {
        @Override
        public int compare(Room r1, Room r2) {
            return (int)(r1.getPricebyDay() - r2.getPricebyDay());
        }
    };

    public static Comparator<Room> DescendingPriceComparator = new Comparator<Room>() {
        @Override
        public int compare(Room r1, Room r2) {
            return (int)(r2.getPricebyDay() - r1.getPricebyDay());
        }
    };

}
