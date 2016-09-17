package com.urja.motoservice.utils;

import com.urja.motoservice.model.Customer;

/**
 * Created by BAPI1 on 9/2/2016.
 */
public interface AppConstants {
    String CAR_ID = "car_id";
    String VEHICLE_GRID_TITLE = "Vehicle Number :";
    String WASH_DETAILING = "WashDetailing";
    String TYRE_WHEEL = "TyreWheel";
    String SERVICE_REPAIR = "ServiceRepair";
    String ACCESSORIES = "Accessories";
    String DENT_PAINT = "DentPaint";
    int BACK_FROM_CHILD_MODIFY_CHOOSEN_SERVICE_ACTIVITY = 7377;

    public interface TableColumns{
        public interface CustomerTable{//Make sure all the name is same as of the Models name
            String NAME = "name";
            String MOBILE = "mobile";
        }
    }
}
