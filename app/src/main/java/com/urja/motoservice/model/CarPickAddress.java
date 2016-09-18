package com.urja.motoservice.model;

/**
 * Created by BAPI1 on 9/18/2016.
 */
public class CarPickAddress {
    private String addressLine1;
    private String addressLine2;
    private String carNumber;//long
    private String city;
    private String landmark;
    private String mobileNumber;
    private String pin;//long
    private String state;

    public CarPickAddress() {
    }

    public CarPickAddress(String addressLine1, String addressLine2, String carNumber, String city, String landmark, String mobileNumber, String pin, String state) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.carNumber = carNumber;
        this.city = city;
        this.landmark = landmark;
        this.mobileNumber = mobileNumber;
        this.pin = pin;
        this.state = state;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
