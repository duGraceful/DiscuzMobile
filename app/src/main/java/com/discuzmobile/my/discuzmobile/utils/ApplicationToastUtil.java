package com.discuzmobile.my.discuzmobile.utils;

import android.content.Context;
import android.widget.Toast;

import com.discuzmobile.my.discuzmobile.app.MyApplication;

public class ApplicationToastUtil {

    private static Toast toast;

    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public static void showToastSimple(String content) {
        showToast(MyApplication.getInstance(), content);
    }
}
