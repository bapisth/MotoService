package com.urja.motoservice.utils;

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
    String CHAR_SPLITER = "#||@@@||#";
    String CAR_CARE_DETAILING = "CarCareDetailing";

    String CAR_TYPE_NAME = "CarTypeName";
    String TRANSACTIOIN_ID = "transaction_id";
    String STATUS_OPEN = "open";
    String STATUS_PROGRESS = "progress";
    String STATUS_CLOSED = "progress";
    String CAR_SMALL = "s";
    String CAR_MEDIUM = "m";
    String CAR_LARGE = "l";

    interface TableColumns {
        interface CustomerTable {//Make sure all the name is same as of the Models name
            String NAME = "name";
            String MOBILE = "mobile";
        }
    }

    interface Transaction{
        String COLUMN_CARPICKADDRESS = "CarPickAddress";
        String COLUMN_CARNUMBER = "CarNumber";
        String COLUMN_SERVICE_REQUESTLIST = "serviceRequestList";
        String COLUMN_SERVICE_REQUEST_DATE= "serviceRequestDate";
        String COLUMN_REQUEST_STATUS= "requestStatus";

    }
    interface AdminNotification{
        String isUnread = "isUnread";
        String customerName = "customerName";
        String customerTransactionId = "customerTransactionId";
        String customerId = "customerId";
    }
    interface ValidVehicle{
        String CAR_TYPE_SMALL = "small";
        String CAR_TYPE_MEDIUM = "medium";
        String CAR_TYPE_LARGE = "large";
        String VALID_VEHICLE_DESC = "desc";
        String VALID_VEHICLE_SIZE = "size";
    }

}
