package com.example.kycadmin;

public class User {
    String name;
    String phone;
    String user_id;
    int isApproved;

    public User(String name, String phone, int isApproved) {
        this.name = name;
        this.phone = phone;
        this.isApproved = isApproved;
    }

    public User(){

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    public static final User[] users = {
            new User("Ram Roshan Ramanathan", "9999999999", 0),
            new User("Rohan Thoman Matthew", "9999999987", 1),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
            new User("Md Arbaaz Khan", "9999999965", 2),
    };

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getIsApproved() {
        return isApproved;
    }
}
