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
    public int empAvail;

    public Employee(String empID, String empIDCard, String empFirstName, String empLastName,
                    String empPhone, String empPosition, String empGender, String empBirthday,
                    String empMail, String empAddress, int empAvail) {
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
    public Employee(){
        this.empID = "";
        this.empIDCard = "";
        this.empFirstName = "";
        this.empLastName = "";
        this.empPhone = "";
        this.empPosition = "";
        this.empGender = "";
        this.empBirthday = "";
        this.empMail = "";
        this.empAddress = "";
        this.empAvail = 1;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getEmpIDCard() {
        return empIDCard;
    }

    public void setEmpIDCard(String empIDCard) {
        this.empIDCard = empIDCard;
    }

    public String getEmpFirstName() {
        return empFirstName;
    }

    public void setEmpFirstName(String empFirstName) {
        this.empFirstName = empFirstName;
    }

    public String getEmpLastName() {
        return empLastName;
    }

    public void setEmpLastName(String empLastName) {
        this.empLastName = empLastName;
    }

    public String getEmpPhone() {
        return empPhone;
    }

    public void setEmpPhone(String empPhone) {
        this.empPhone = empPhone;
    }

    public String getEmpPosition() {
        return empPosition;
    }

    public void setEmpPosition(String empPosition) {
        this.empPosition = empPosition;
    }

    public String getEmpGender() {
        return empGender;
    }

    public void setEmpGender(String empGender) {
        this.empGender = empGender;
    }

    public String getEmpBirthday() {
        return empBirthday;
    }

    public void setEmpBirthday(String empBirthday) {
        this.empBirthday = empBirthday;
    }

    public String getEmpMail() {
        return empMail;
    }

    public void setEmpMail(String empMail) {
        this.empMail = empMail;
    }

    public String getEmpAddress() {
        return empAddress;
    }

    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }

    public int getEmpAvail() {
        return empAvail;
    }

    public void setEmpAvail(int empAvail) {
        this.empAvail = empAvail;
    }
}
