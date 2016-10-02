package com.urja.motoservice.messaging;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by BAPI1 on 10/1/2016.
 */
public class UrjaFirebaseInstanceIDService extends FirebaseInstanceIdService {


    private static final String TAG = UrjaFirebaseInstanceIDService.class.getSimpleName();
    private String REG_TOKEN = "";

    public String getREG_TOKEN() {
        return REG_TOKEN;
    }

    public void setREG_TOKEN(String REG_TOKEN) {
        this.REG_TOKEN = REG_TOKEN;
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        REG_TOKEN = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh: While ForeGround : " + REG_TOKEN);
    }


}
