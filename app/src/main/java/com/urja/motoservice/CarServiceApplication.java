package com.urja.motoservice;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by BAPI1 on 9/11/2016.
 */
public class CarServiceApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/angelina.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
