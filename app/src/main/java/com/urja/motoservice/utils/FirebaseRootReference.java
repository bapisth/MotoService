package com.urja.motoservice.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by BAPI1 on 9/18/2016.
 */
public class FirebaseRootReference {
    private volatile static FirebaseRootReference _instance;
    private DatabaseReference mDatabaseRootRef;
    private DatabaseReference mCustomerDatabaseRef;
    private DatabaseReference mTransactionDatabaseRef;
    private DatabaseReference mCustomerAddressDatabaseRef;
    private DatabaseReference mVehicleTypesRef;
    private DatabaseReference mAdminNotificationRef;
    private DatabaseReference mCarCareDetailingRef;
    private DatabaseReference mTransactionIdRef;



    private FirebaseRootReference() {
        //Preventing Singletone Object instantiation from Outside
    }

    public static FirebaseRootReference getInstance() {
        if (_instance == null) {
            synchronized (FirebaseRootReference.class) {
                if (_instance == null) {
                    _instance = new FirebaseRootReference();
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    _instance.setmDatabaseRootRef(rootRef);
                    _instance.setmCustomerDatabaseRef(rootRef.child(DatabaseConstants.TABLE_CUSTOMER));
                    _instance.setmCustomerAddressDatabaseRef(rootRef.child(DatabaseConstants.TABLE_CUSTOMER_ADDRESS));
                    _instance.setmTransactionDatabaseRef(rootRef.child(DatabaseConstants.TABLE_TRANSACTION));
                    _instance.setmVehicleTypesRef(rootRef.child(DatabaseConstants.TABLE_VEHICLE + "/" + DatabaseConstants.TABLE_VEHICLE_TYPE));
                    _instance.setmAdminNotificationRef(rootRef.child(DatabaseConstants.TABLE_ADMIN_NOTIFICATION));
                    _instance.setmCarCareDetailingRef(rootRef.child(DatabaseConstants.TABLE_CAR_SERVICE+"/"+DatabaseConstants.CAR_CARE_DETAILING));
                    _instance.setmTransactionIdRef(rootRef.child(DatabaseConstants.TABLE_TRANSACTION_ID_REF));
                }
            }
        }
        return _instance;
    }

    public static FirebaseRootReference get_instance() {
        return _instance;
    }

    public static void set_instance(FirebaseRootReference _instance) {
        FirebaseRootReference._instance = _instance;
    }

    public DatabaseReference getmDatabaseRootRef() {
        return mDatabaseRootRef;
    }

    public void setmDatabaseRootRef(DatabaseReference mDatabaseRootRef) {
        this.mDatabaseRootRef = mDatabaseRootRef;
    }

    public DatabaseReference getmCustomerDatabaseRef() {
        return mCustomerDatabaseRef;
    }

    public void setmCustomerDatabaseRef(DatabaseReference mCustomerDatabaseRef) {
        this.mCustomerDatabaseRef = mCustomerDatabaseRef;
    }

    public DatabaseReference getmTransactionDatabaseRef() {
        return mTransactionDatabaseRef;
    }

    public void setmTransactionDatabaseRef(DatabaseReference mTransactionDatabaseRef) {
        this.mTransactionDatabaseRef = mTransactionDatabaseRef;
    }

    public DatabaseReference getmCustomerAddressDatabaseRef() {
        return mCustomerAddressDatabaseRef;
    }

    public void setmCustomerAddressDatabaseRef(DatabaseReference mCustomerAddressDatabaseRef) {
        this.mCustomerAddressDatabaseRef = mCustomerAddressDatabaseRef;
    }

    public DatabaseReference getmVehicleTypesRef() {
        return mVehicleTypesRef;
    }

    public void setmVehicleTypesRef(DatabaseReference mVehicleTypesRef) {
        this.mVehicleTypesRef = mVehicleTypesRef;
    }

    public DatabaseReference getmAdminNotificationRef() {
        return mAdminNotificationRef;
    }

    public void setmAdminNotificationRef(DatabaseReference mAdminNotificationRef) {
        this.mAdminNotificationRef = mAdminNotificationRef;
    }

    public DatabaseReference getmCarCareDetailingRef() {
        return mCarCareDetailingRef;
    }

    public void setmCarCareDetailingRef(DatabaseReference mCarCareDetailingRef) {
        this.mCarCareDetailingRef = mCarCareDetailingRef;
    }

    public DatabaseReference getmTransactionIdRef() {
        return mTransactionIdRef;
    }

    public void setmTransactionIdRef(DatabaseReference mTransactionIdRef) {
        this.mTransactionIdRef = mTransactionIdRef;
    }
}
