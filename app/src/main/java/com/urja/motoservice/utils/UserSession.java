package com.urja.motoservice.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class UserSession {
    private static UserSession userSession;
    private String mCurrentUserDisplayName;
    private String mCurrentUserId;
    private String mCurrentUserEmail;
    private static final String DISPLAY_NAME = "displayName";
    private static final String USER_ID = "userId";
    private static final String EMAIL = "email";

    public static UserSession getUserSession() {
        return userSession;
    }

    public static void setUserSession(UserSession userSession) {
        UserSession.userSession = userSession;
    }

    public String getCurrentUserDisplayName() {
        return mCurrentUserDisplayName;
    }

    public void setCurrentUserDisplayName(String currentUserDisplayName) {
        mCurrentUserDisplayName = currentUserDisplayName;
    }

    public String getCurrentUserId() {
        return mCurrentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        mCurrentUserId = currentUserId;
    }

    public String getCurrentUserEmail() {
        return mCurrentUserEmail;
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        mCurrentUserEmail = currentUserEmail;
    }

    public static void setSession(UserSession session, Context context) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear();
        editor.putString(DISPLAY_NAME, session.getCurrentUserDisplayName());
        editor.putString(USER_ID, session.getCurrentUserId());
        editor.putString(EMAIL, session.getCurrentUserEmail());
        editor.commit();
        editor.apply();
    }

    public static UserSession getSession(Context context) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear();
        UserSession session = new UserSession();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        session.setCurrentUserDisplayName(settings.getString(DISPLAY_NAME, ""));
        session.setCurrentUserEmail(settings.getString(EMAIL, ""));
        session.setCurrentUserId(settings.getString(USER_ID, ""));
        return session;
    }


}
