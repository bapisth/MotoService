package com.urja.motoservice.model;

/**
 * Created by BAPI1 on 8/16/2016.
 */

public class Vehicle {
    private Long code;
    private String downloadPath;
    private String path;
    private String carType;

    public Vehicle() {
    }

    public Vehicle(Long code, String downloadPath, String path, String carType) {
        this.code = code;
        this.downloadPath = downloadPath;
        this.path = path;
        this.carType = carType;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }
}
