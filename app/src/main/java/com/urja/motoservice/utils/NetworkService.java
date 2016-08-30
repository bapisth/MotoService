package com.urja.motoservice.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class NetworkService extends BroadcastReceiver {
    public static int Bandwidth;
    public static boolean Network;

    static {
        Network = false;
        Bandwidth = 0;
    }

    public void onReceive(Context context, Intent intent) {
        NetworkInfo activeNetInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();//"connectivity"
        TelephonyManager telephonyManager;
        if (activeNetInfo != null) {
            Network = activeNetInfo.isConnectedOrConnecting();
            if (1 == activeNetInfo.getType()) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);//"wifi"
                if (wifiManager == null || wifiManager.getConnectionInfo() == null) {
                    Network = false;
                    Bandwidth = 0;
                    return;
                }
                Bandwidth = wifiManager.getConnectionInfo().getLinkSpeed();
                return;
            } else if (activeNetInfo.getType() == 0) {
                telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); //"phone"
                if (telephonyManager != null) {
                    Bandwidth = telephonyManager.getNetworkType();
                    return;
                }
                Network = false;
                Bandwidth = 0;
                return;
            } else {
                return;
            }
        }
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); //"phone"
        if (telephonyManager == null) {
            Network = false;
            Bandwidth = 0;
        } else if (telephonyManager.getDataActivity() != 0) {
            Network = true;
            Bandwidth = telephonyManager.getNetworkType();
        } else {
            Network = false;
            Bandwidth = 0;
        }
    }

    public static void Initialize(Context context) {
        NetworkInfo activeNetInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();//"connectivity"
        if (activeNetInfo != null) {
            Network = activeNetInfo.isConnectedOrConnecting();
            if (1 == activeNetInfo.getType()) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);//"wifi"
                if (wifiManager != null && wifiManager.getConnectionInfo() != null) {
                    Bandwidth = wifiManager.getConnectionInfo().getLinkSpeed();
                }
            } else if (activeNetInfo.getType() == 0) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); //"phone"
                if (telephonyManager != null) {
                    Bandwidth = telephonyManager.getNetworkType();
                }
            }
        }
    }
}
