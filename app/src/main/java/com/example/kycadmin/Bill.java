package com.example.kycadmin;

public class Bill {

    String bill;
    String date;
    String enterprise_id;
    String enterprise_name;

    public Bill(String bill, String date, String enterprise_id, String enterprise_name) {
        this.bill = bill;
        this.date = date;
        this.enterprise_id = enterprise_id;
        this.enterprise_name = enterprise_name;
    }

    public Bill() {
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEnterprise_id() {
        return enterprise_id;
    }

    public void setEnterprise_id(String enterprise_id) {
        this.enterprise_id = enterprise_id;
    }

    public String getEnterprise_name() {
        return enterprise_name;
    }

    public void setEnterprise_name(String enterprise_name) {
        this.enterprise_name = enterprise_name;
    }
}
