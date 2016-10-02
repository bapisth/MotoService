package com.urja.motoservice.model;

/**
 * Created by hemendra on 26-07-2016.
 */
public class Customer {
    private String name;
    private String mobile;
    private String regToken;

    public Customer() {
    }

    public Customer(String name, String mobile, String regToken) {
        this.name = name;
        this.mobile = mobile;
        this.regToken = regToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegToken() {
        return regToken;
    }

    public void setRegToken(String regToken) {
        this.regToken = regToken;
    }
}
