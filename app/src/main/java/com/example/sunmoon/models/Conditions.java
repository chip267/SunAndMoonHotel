package com.example.sunmoon.models;

public class Conditions {
    public String cReportID;
    public String cRoomNo;
    public String cState;
    public String cName;
    public String cDate;
    public String cAvail;

    public Conditions(String cReportID, String cRoomNo, String cState, String cName,
                      String cDate, String cAvail) {
        this.cReportID = cReportID;
        this.cRoomNo = cRoomNo;
        this.cState = cState;
        this.cName = cName;
        this.cDate = cDate;
        this.cAvail = cAvail;
    }
    public String getCReportID() {
        return cReportID;
    }

    public void setCReportID(String cReportID) {
        this.cReportID = cReportID;
    }

    public String getCRoomNo() {
        return cRoomNo;
    }

    public void setCRoomNo(String cRoomNo) {
        this.cRoomNo = cRoomNo;
    }

    public String getCState() {
        return cState;
    }

    public void setCState(String cState) {
        this.cState = cState;
    }

    public String getCName() {
        return cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public String getCDate() {
        return cDate;
    }

    public void setCDate(String cDate) {
        this.cDate = cDate;
    }

    public String getCAvail() {
        return cAvail;
    }

    public void setCAvail(String cAvail) {
        this.cAvail = cAvail;
    }
}
