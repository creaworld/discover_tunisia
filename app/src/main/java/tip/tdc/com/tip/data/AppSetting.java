/*
 * Copyright (c) 2016. Created by PNT
 */

package tip.tdc.com.tip.data;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

import tip.tdc.com.tip.utils.MSharedPreferences;

public class AppSetting {

    private static AppSetting appSetting;
    private static Context mContext;
    MSharedPreferences mSharedPreferences;
    public static int LIMIT_SAVED_ITEM = 10000;

    public static final String KEY_IS_FIRST_LAUNCH = "KEY_IS_FIRST_LAUNCH";
    public static final String KEY_CITY_UNLOCKED = "KEY_CITY_UNLOCKED";
    public static final String KEY_COUNT_OPEN = "KEY_COUNT_OPEN";
    public static final String KEY_APP_RATED = "KEY_APP_RATED";

//    private String KEY_LAST_UPDATE_TRANSFER_LIMITATION = "KEY_LAST_UPDATE_TRANSFER_LIMITATION";

    private void initSetting() {
        mSharedPreferences = MSharedPreferences.getInstance(mContext);
    }

    public static AppSetting getInstant(Context mContext) {
        if (appSetting == null) {
            appSetting = new AppSetting();
            appSetting.mContext = mContext;
            appSetting.initSetting();
        }
        return appSetting;
    }

    public MSharedPreferences getmSharedPreferences() {
        return mSharedPreferences;
    }

    public void setmSharedPreferences(MSharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    int expiresIn = 0;
    Date expiresTime = Calendar.getInstance().getTime();

    public Date getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(Date expiresTime) {
        this.expiresTime = expiresTime;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public boolean isCityUnlocked(String cityKey) {
        return mSharedPreferences.getBoolean(KEY_CITY_UNLOCKED + cityKey, false);
    }

    public void setCityUnlocked(String cityKey) {
        mSharedPreferences.putBoolean(KEY_CITY_UNLOCKED + cityKey, true);
    }

    public boolean isUnlockAllCity() {
        return mSharedPreferences.getBoolean(KEY_CITY_UNLOCKED + "all_city", false);
    }

    public void setUnlockAllCity(boolean unlockAllCity) {
        mSharedPreferences.putBoolean(KEY_CITY_UNLOCKED + "all_city", unlockAllCity);
    }

    public int getOpenCount() {
        return mSharedPreferences.getInt(KEY_COUNT_OPEN, 0);
    }

    public void setOpenCount(int openCount) {
        mSharedPreferences.putInt(KEY_COUNT_OPEN, openCount);
    }

    boolean appRated;

    public boolean isAppRated() {
        return mSharedPreferences.getBoolean(KEY_APP_RATED, false);
    }

    public void setAppRated(boolean appRated) {
        mSharedPreferences.putBoolean(KEY_APP_RATED, appRated);
    }
}
