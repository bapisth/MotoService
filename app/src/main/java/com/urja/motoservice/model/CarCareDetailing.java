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


