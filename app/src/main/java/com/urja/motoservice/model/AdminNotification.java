package com.urja.motoservice.model;

/**
 * Created by BAPI1 on 9/25/2016.
 */
public class AdminNotification {
    boolean isUnread;
    String customerName;
    String customerId;
    String transactionId;
    String customerVehicleNumber;
    String date;
    String rootRef;
    String transactionRef;

    public AdminNotification() {
    }

    public AdminNotification(boolean isUnread, String customerName, String customerId, String transactionId, String customerVehicleNumber, String date, String rootRef, String transactionRef) {
        this.isUnread = isUnread;
        this.customerName = customerName;
        this.customerId = customerId;
        this.transactionId = transactionId;
        this.customerVehicleNumber = customerVehicleNumber;
        this.date = date;
        this.rootRef = rootRef;
        this.transactionRef = transactionRef;
    }

    public boolean isUnread() {
        return isUnread;
    }

    public void setUnread(boolean unread) {
        isUnread = unread;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustomerVehicleNumber() {
        return customerVehicleNumber;
    }

    public void setCustomerVehicleNumber(String customerVehicleNumber) {
        this.customerVehicleNumber = customerVehicleNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRootRef() {
        return rootRef;
    }

    public void setRootRef(String rootRef) {
        this.rootRef = rootRef;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }
}
