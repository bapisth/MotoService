package com.urja.motoservice.model;

/**
 * Created by BAPI1 on 9/18/2016.
 */
public class ProfileUpdatedEvent {
    private boolean profileUpdated;
    private Customer customer;

    public ProfileUpdatedEvent() {
    }

    public ProfileUpdatedEvent(boolean profileUpdated, Customer customer) {
        this.profileUpdated = profileUpdated;
        this.customer = customer;
    }

    public boolean isProfileUpdated() {
        return profileUpdated;
    }

    public void setProfileUpdated(boolean profileUpdated) {
        this.profileUpdated = profileUpdated;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
