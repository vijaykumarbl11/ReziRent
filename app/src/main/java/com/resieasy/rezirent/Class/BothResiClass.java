package com.resieasy.rezirent.Class;

public class BothResiClass {

    String id,status,rtype;

    public BothResiClass() {
    }

    public BothResiClass(String id, String status, String rtype) {
        this.id = id;
        this.status = status;
        this.rtype = rtype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRtype() {
        return rtype;
    }

    public void setRtype(String rtype) {
        this.rtype = rtype;
    }
}
