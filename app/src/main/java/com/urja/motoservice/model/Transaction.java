package com.urja.motoservice.model;

import com.urja.motoservice.database.ServiceRequest;

import java.util.List;

/**
 * Created by BAPI1 on 9/18/2016.
 */
public class Transaction {
    private CarPickAddress CarPickAddress;
    private String requestStatus;
    private String serviceRequestDate;
    private List<ServiceRequest> serviceRequestList;
    private String transactionId;
    private String totalAmount;

    public Transaction() {
    }

    public Transaction(com.urja.motoservice.model.CarPickAddress carPickAddress, String requestStatus, String serviceRequestDate, List<ServiceRequest> serviceRequestList, String transactionId, String totalAmount) {
        CarPickAddress = carPickAddress;
        this.requestStatus = requestStatus;
        this.serviceRequestDate = serviceRequestDate;
        this.serviceRequestList = serviceRequestList;
        this.transactionId = transactionId;
        this.totalAmount = totalAmount;
    }

    public com.urja.motoservice.model.CarPickAddress getCarPickAddress() {
        return CarPickAddress;
    }

    public void setCarPickAddress(com.urja.motoservice.model.CarPickAddress carPickAddress) {
        CarPickAddress = carPickAddress;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getServiceRequestDate() {
        return serviceRequestDate;
    }

    public void setServiceRequestDate(String serviceRequestDate) {
        this.serviceRequestDate = serviceRequestDate;
    }

    public List<ServiceRequest> getServiceRequestList() {
        return serviceRequestList;
    }

    public void setServiceRequestList(List<ServiceRequest> serviceRequestList) {
        this.serviceRequestList = serviceRequestList;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
