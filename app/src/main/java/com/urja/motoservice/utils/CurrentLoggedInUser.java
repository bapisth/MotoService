package com.urja.motoservice.utils;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by BAPI1 on 8/14/2016.
 */

public class CurrentLoggedInUser {
    public static FirebaseUser sCurrentFirebaseUser;
    public static String sName;
    public static String sMobile;

    public static FirebaseUser getCurrentFirebaseUser() {
        return sCurrentFirebaseUser;
    }

    public static void setCurrentFirebaseUser(FirebaseUser currentFirebaseUser) {
        sCurrentFirebaseUser = currentFirebaseUser;
    }

    public static String getName() {
        return sName;
    }

    public static void setName(String name) {
        sName = name;
    }

    public static String getMobile() {
        return sMobile;
    }

    public static void setMobile(String mobile) {
        sMobile = mobile;
    }
}
