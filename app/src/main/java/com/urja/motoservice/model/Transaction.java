package com.urja.motoservice.model;

import com.urja.motoservice.database.ServiceRequest;

import java.util.List;

/**
 * Created by BAPI1 on 9/18/2016.
 */
public class Transaction {
    private CarPickAddress CarPickAddress;
    private boolean requestOpen;
    private ServiceRequestDate serviceRequestDate;
    private List<ServiceRequest> serviceRequestList;

    public Transaction() {
    }

    public Transaction(com.urja.motoservice.model.CarPickAddress carPickAddress, boolean requestOpen, ServiceRequestDate serviceRequestDate, List<ServiceRequest> serviceRequestList) {
        CarPickAddress = carPickAddress;
        this.requestOpen = requestOpen;
        this.serviceRequestDate = serviceRequestDate;
        this.serviceRequestList = serviceRequestList;
    }

    public com.urja.motoservice.model.CarPickAddress getCarPickAddress() {
        return CarPickAddress;
    }

    public void setCarPickAddress(com.urja.motoservice.model.CarPickAddress carPickAddress) {
        CarPickAddress = carPickAddress;
    }

    public boolean isRequestOpen() {
        return requestOpen;
    }

    public void setRequestOpen(boolean requestOpen) {
        this.requestOpen = requestOpen;
    }

    public ServiceRequestDate getServiceRequestDate() {
        return serviceRequestDate;
    }

    public void setServiceRequestDate(ServiceRequestDate serviceRequestDate) {
        this.serviceRequestDate = serviceRequestDate;
    }

    public List<ServiceRequest> getServiceRequestList() {
        return serviceRequestList;
    }

    public void setServiceRequestList(List<ServiceRequest> serviceRequestList) {
        this.serviceRequestList = serviceRequestList;
    }
}
