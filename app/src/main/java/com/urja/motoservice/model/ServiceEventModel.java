package com.urja.motoservice.model;

/**
 * Created by BAPI1 on 8/20/2016.
 */

public class ServiceEventModel {
    private boolean mAdd;
    private Object mObject;

    public ServiceEventModel() {
    }

    public ServiceEventModel(boolean add, Object object) {
        mAdd = add;
        mObject = object;
    }

    public boolean isAdd() {
        return mAdd;
    }

    public void setAdd(boolean add) {
        mAdd = add;
    }

    public Object getObject() {
        return mObject;
    }

    public void setObject(Object object) {
        mObject = object;
    }
}
