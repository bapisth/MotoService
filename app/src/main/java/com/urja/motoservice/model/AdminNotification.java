package com.urja.motoservice.model;

/**
 * Created by BAPI1 on 9/25/2016.
 */
public class AdminNotification {
    boolean isUnread;
    String customerName;
    String customerId;
    String transactionId;

    public AdminNotification() {
    }

    public AdminNotification(boolean isUnread, String customerName, String customerId, String transactionId) {
        this.isUnread = isUnread;
        this.customerName = customerName;
        this.customerId = customerId;
        this.transactionId = transactionId;
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
}
