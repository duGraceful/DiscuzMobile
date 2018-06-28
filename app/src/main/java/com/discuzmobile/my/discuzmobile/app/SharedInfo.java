package com.discuzmobile.my.discuzmobile.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.discuzmobile.my.discuzmobile.activity.GuiDanceActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 */
public class SharedInfo {

    private final static String SHARED_PREFERENCES_TAG = "LOCAL_DB";

    // "ISO-8859-1", 单字节编码方式
    // private final static String FORMAT = "ISO-8859-1";

    /**
     * 是否第一次打开APP
     */
    private Boolean IsOpen = true;

    public static final Map<Object, Object> activityJson = new HashMap<Object, Object>();
    private static SharedInfo sSharedInfo;
    public static Editor editor;
    private final MyApplication app;
    private final SharedPreferences sharedPreferences;

    /**
     * 单例
     */
    public static SharedInfo getInstance() {
        if (sSharedInfo == null) {
            sSharedInfo = new SharedInfo();
        }
        return sSharedInfo;
    }


    private SharedInfo() {
        app = MyApplication.getInstance();
        sharedPreferences = app.getSharedPreferences(SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    /**
     * 设置是否第一次登录
     *
     * @param isLast
     */
    public void putLastLogin(boolean isLast) {
        editor.putBoolean(GuiDanceActivity.LAST_LOGN, isLast);
        editor.commit();
    }

    /**
     * 设置是否获取权限
     */
    public void setIsGetPar(boolean isPar) {
        editor.putBoolean("isGetPar", isPar);
        editor.commit();
    }

    /**
     * 获取是否获取过权限
     *
     * @return
     */
    public boolean getIsGetPar() {
        return sharedPreferences.getBoolean("isGetPar", false);
    }

    /**
     * 获取是否第一次登录
     *
     * @return
     */
    public boolean getLastLogin() {
        return sharedPreferences.getBoolean(GuiDanceActivity.LAST_LOGN, false);
    }

    //判断用户是否第一次进入
    public void setrun(Boolean ture) {
        if (ture) {
            editor.putBoolean("run", true);
        } else {
            editor.putBoolean("run", false);
        }
        editor.commit();
    }

    public boolean getrun() {
        return sharedPreferences.getBoolean("run", false);
    }


    public void setIsFingerPrint(Boolean code) {
        editor.putBoolean("IsFingerPrint", code).commit();
    }


    /***
     * basic IO1 (String): get, put
     */
    private synchronized String basicGetString(final String key) {
        return sharedPreferences.getString(key, null);
    }

    private synchronized void basicPutString(final String key, final String value) {
        final Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * 类对应的key
     */
    private String getKey(final Class<?> clazz) {
        if (clazz != null) {
            return clazz.getSimpleName();
        }
        return null;
    }

    private String combineKeys(final String k0, final String k1) {
        return new StringBuilder().append(k0).append("-").append(k1).toString();
    }

}
