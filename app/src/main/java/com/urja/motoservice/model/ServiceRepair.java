package com.urja.motoservice.model;

/**
 * Created by BAPI1 on 8/19/2016.
 */

public class ServiceRepair {
    private String code;
    private String desc;

    public ServiceRepair() {
    }

    public ServiceRepair(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
