package com.resieasy.rezirent.Class;

public class SingleIDClass {


    String id,type,city,f1,f2;
    int i1;
    Long time;

    public SingleIDClass() {
    }

    public SingleIDClass(String id, String type, String city, String f1, String f2, int i1, Long time) {
        this.id = id;
        this.type = type;
        this.city = city;
        this.f1 = f1;
        this.f2 = f2;
        this.i1 = i1;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
