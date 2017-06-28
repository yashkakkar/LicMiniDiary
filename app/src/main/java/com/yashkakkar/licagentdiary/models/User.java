package com.yashkakkar.licagentdiary.models;

/**
 * Created by Yash Kakkar on 20-05-2017.
 */


public class User {

    private String name;
    private String email;
    private String pass;
    private String address;
    private String gender;
    private String phoneNumber;
    private String imagePath;

    public User(){

    }

    public User(String name,String email, String pass) {
        this.name = name;
        this.email = email;
        this.pass = pass;
    }

    public User(String name, String email, String pass, String address, String gender, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.address = address;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getimagePath() {
        return imagePath;
    }

    public void setimagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
