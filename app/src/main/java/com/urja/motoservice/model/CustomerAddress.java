package com.urja.motoservice.model;

/**
 * Created by hemendra on 26-07-2016.
 */
public class CustomerAddress {
    private String address;
    private String street;
    private String pincode;
    private String country="India";

    public CustomerAddress() {
    }

    public CustomerAddress(String address, String street, String pincode, String country) {
        this.address = address;
        this.street = street;
        this.pincode = pincode;
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
