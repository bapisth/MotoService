package com.urja.motoservice.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.urja.motoservice.database.dao.CarServicePriceDao;
import com.urja.motoservice.database.dao.DaoMaster;
import com.urja.motoservice.database.dao.DaoSession;
import com.urja.motoservice.database.dao.ServiceRequestDao;
import com.urja.motoservice.database.dao.ValidVehicleDao;

/**
 * Created by BAPI1 on 8/20/2016.
 */

public class DbHelper {
    public static DbHelper instance;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SQLiteDatabase db;
    private DaoMaster.DevOpenHelper helper;

    static {
        instance = null;
    }

    public DbHelper(Context context) {
        this.helper = null;
        this.db = null;
        this.daoMaster = null;
        this.daoSession = null;
        this.helper = new DaoMaster.DevOpenHelper(context, "UrjaCarService", null);
        this.db = this.helper.getWritableDatabase();
        this.daoMaster = new DaoMaster(this.db);
        this.daoSession = this.daoMaster.newSession();
    }

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    }

    public DaoSession getDaoSession() {
        return this.daoSession;
    }

    public ServiceRequestDao getServiceRequestDao() {
        return this.daoSession.getServiceRequestDao();
    }

    public CarServicePriceDao getCarServicePriceDao(){
        return this.daoSession.getCarServicePriceDao();
    }

    public ValidVehicleDao  getValidVehicleDao(){
        return this.daoSession.getValidVehicleDao();
    }
}
