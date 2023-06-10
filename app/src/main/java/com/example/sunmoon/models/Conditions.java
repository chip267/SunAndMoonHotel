package com.example.sunmoon.models;

public class Conditions {
    public String reportID;
    public String roomNo;
    public String state;
    public String name;
    public String date;
    public int avail;
    public Conditions() {
        /*this.reportID = "c000";
        this.roomNo = "900";
        this.state = "Dirty";
        this.name = "Tui";
        this.date = "11:11 26/08/2003";
        this.avail = 1;*/
    }

    public Conditions(String reportID, String roomNo, String state, String name,
                      String date, int avail) {
        this.reportID = reportID;
        this.roomNo = roomNo;
        this.state = state;
        this.name = name;
        this.date = date;
        this.avail = avail;
    }
    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAvail() {
        return avail;
    }

    public void setAvail(int avail) {
        this.avail = avail;
    }
}
