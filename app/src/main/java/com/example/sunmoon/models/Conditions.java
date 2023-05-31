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
}
