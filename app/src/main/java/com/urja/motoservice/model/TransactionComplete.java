package com.urja.motoservice.model;

/**
 * Created by BAPI1 on 9/5/2016.
 */
public class TransactionComplete {
    private boolean transactionComplete = false;
    private String transactionId;

    public TransactionComplete() {
    }

    public TransactionComplete(boolean transactionComplete, String transactionId) {
        this.transactionComplete = transactionComplete;
        this.transactionId = transactionId;
    }

    public boolean isTransactionComplete() {
        return transactionComplete;
    }

    public void setTransactionComplete(boolean transactionComplete) {
        this.transactionComplete = transactionComplete;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
