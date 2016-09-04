package com.urja.motoservice.model;

/**
 * Created by BAPI1 on 8/16/2016.
 */

public class Vehicle {
    private Long code;
    private String downloadPath;
    private String path;

    public Vehicle() {
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Vehicle(Long code, String downloadPath, String path) {
        this.code = code;
        this.downloadPath = downloadPath;
        this.path = path;
    }

    public Long getCode() {
        return code;
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
}
