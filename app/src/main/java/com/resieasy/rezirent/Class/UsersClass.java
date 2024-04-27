package com.resieasy.rezirent.Class;

public class UsersClass {

    String name,mail,number,city,token,userid,f1,f2;
    int i1;

    public UsersClass() {
    }

    public UsersClass(String name, String mail, String number, String city, String token, String userid, String f1, String f2, int i1) {
        this.name = name;
        this.mail = mail;
        this.number = number;
        this.city = city;
        this.token = token;
        this.userid = userid;
        this.f1 = f1;
        this.f2 = f2;
        this.i1 = i1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getF1() {
        return f1;
    }

    public void setF1(String f1) {
        this.f1 = f1;
    }

    public String getF2() {
        return f2;
    }

    public void setF2(String f2) {
        this.f2 = f2;
    }

    public int getI1() {
        return i1;
    }

    public void setI1(int i1) {
        this.i1 = i1;
    }
}
