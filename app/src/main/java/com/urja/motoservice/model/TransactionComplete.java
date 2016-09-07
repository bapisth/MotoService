package com.urja.motoservice.model;

/**
 * Created by BAPI1 on 9/5/2016.
 */
public class TransactionComplete {
    private boolean transactionComplete = false;

    public TransactionComplete(boolean transactionComplete) {
        this.transactionComplete = transactionComplete;
    }

    public boolean isTransactionComplete() {
        return transactionComplete;
    }

    public void setTransactionComplete(boolean transactionComplete) {
        this.transactionComplete = transactionComplete;
    }
}
