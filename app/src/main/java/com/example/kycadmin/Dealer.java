package com.example.kycadmin;

public class Dealer {

    String aadhar;
    String addr1;
    String addr2;
    String addr3;
    String contact;
    String enterpriseName;
    String gstin;
    String name;
    String pan;
    String addedBy;
    Double lat;
    Double lon;
    String id;

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Dealer() {
    }

    public Dealer(String aadhar, String addr1, String addr2, String addr3, String contact, String enterpriseName, String GSTIN, String name, String PAN, String addedBy, Double lat, Double lon, String id) {
        this.aadhar = aadhar;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.addr3 = addr3;
        this.contact = contact;
        this.enterpriseName = enterpriseName;
        this.gstin = GSTIN;
        this.name = name;
        this.pan = PAN;
        this.addedBy = addedBy;
        this.lat = lat;
        this.lon = lon;
        this.id = id;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getAddr1() {
        return addr1;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getAddr3() {
        return addr3;
    }

    public void setAddr3(String addr3) {
        this.addr3 = addr3;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

}
