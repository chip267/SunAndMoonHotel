package com.example.sunmoon.models;

public class Employee {
    public String empID;
    public String empIDCard;
    public String empFirstName;
    public String empLastName;
    public String empPhone;
    public String empPosition;
    public String empGender;
    public String empBirthday;
    public String empMail;
    public String empAddress;
    public boolean empAvail;

    public Employee(String empID, String empIDCard, String empFirstName, String empLastName,
                    String empPhone, String empPosition, String empGender, String empBirthday,
                    String empMail, String empAddress, boolean empAvail) {
        this.empID = empID;
        this.empIDCard = empIDCard;
        this.empFirstName = empFirstName;
        this.empLastName = empLastName;
        this.empPhone = empPhone;
        this.empPosition = empPosition;
        this.empGender = empGender;
        this.empBirthday = empBirthday;
        this.empMail = empMail;
        this.empAddress = empAddress;
        this.empAvail = empAvail;
    }
}
