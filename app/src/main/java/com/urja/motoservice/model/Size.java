package com.urja.motoservice.model;


public class Size {
    private Long s;
    private Long m;
    private Long l;

    public Size() {
    }

    public Size(Long s, Long m, Long l) {
        this.s = s;
        this.m = m;
        this.l = l;
    }

    public Long getS() {
        return s;
    }

    public void setS(Long s) {
        this.s = s;
    }

    public Long getM() {
        return m;
    }

    public void setM(Long m) {
        this.m = m;
    }

    public Long getL() {
        return l;
    }

    public void setL(Long l) {
        this.l = l;
    }
}

