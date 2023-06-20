package com.example.sunmoon.models;

public class Conditions {
    public String reportID;
    public String roomNo;
    public String state;
    public String name;
    public String date;
    public String statusUpdate;
    public String nameUpdate;
    public String dateUpdate;
    public String imageURL;
    public int avail;
    public Conditions() {

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
    public Conditions(String reportID, String roomNo, String state, String name,
                      String date, String imageURL, String statusUpdate, String nameUpdate,
                      String dateUpdate, int avail) {
        this.reportID = reportID;
        this.roomNo = roomNo;
        this.state = state;
        this.name = name;
        this.date = date;
        this.imageURL = imageURL;
        this.statusUpdate = statusUpdate;
        this.nameUpdate = nameUpdate;
        this.dateUpdate = dateUpdate;
        this.avail = avail;
    }
    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatusUpdate() {
        return statusUpdate;
    }

    public void setStatusUpdate(String statusUpdate) {
        this.statusUpdate = statusUpdate;
    }

    public String getNameUpdate() {
        return nameUpdate;
    }

    public void setNameUpdate(String nameUpdate) {
        this.nameUpdate = nameUpdate;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
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
