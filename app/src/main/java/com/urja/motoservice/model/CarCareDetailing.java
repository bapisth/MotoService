package com.urja.motoservice.model;

/**
 * Created by BAPI1 on 9/23/2016.
 */
public class CarCareDetailing {
    private String desc;
    private String code;
    private Size size;

    public CarCareDetailing() {
    }

    public CarCareDetailing(String desc, String code, Size size) {
        this.desc = desc;
        this.code = code;
        this.size = size;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}

class Size{
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

