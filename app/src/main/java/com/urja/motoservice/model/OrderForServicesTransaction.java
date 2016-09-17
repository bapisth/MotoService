package com.urja.motoservice.model;

import com.urja.motoservice.database.ServiceRequest;

import java.util.Date;
import java.util.List;

/**
 * Created by BAPI1 on 9/11/2016.
 */
public class OrderForServicesTransaction {
    private List<ServiceRequest> serviceRequestList = null;
    private boolean requestOpen = false;
    private Date serviceRequestDate = null;


    public OrderForServicesTransaction() {
    }

    public OrderForServicesTransaction(List<ServiceRequest> serviceRequestList, boolean requestOpen, Date serviceRequestDate) {
        this.serviceRequestList = serviceRequestList;
        this.requestOpen = requestOpen;
        this.serviceRequestDate = serviceRequestDate;
    }

    public List<ServiceRequest> getServiceRequestList() {
        return serviceRequestList;
    }

    public void setServiceRequestList(List<ServiceRequest> serviceRequestList) {
        this.serviceRequestList = serviceRequestList;
    }

    public boolean isRequestOpen() {
        return requestOpen;
    }

    public void setRequestOpen(boolean requestOpen) {
        this.requestOpen = requestOpen;
    }

    public Date getServiceRequestDate() {
        return serviceRequestDate;
    }

    public void setServiceRequestDate(Date serviceRequestDate) {
        this.serviceRequestDate = serviceRequestDate;
    }
}
