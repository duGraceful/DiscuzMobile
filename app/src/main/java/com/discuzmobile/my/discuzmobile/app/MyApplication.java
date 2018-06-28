package com.discuzmobile.my.discuzmobile.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;


public class MyApplication extends Application {
    //分支测试11
    private Boolean isLogin = false;
    private static MyApplication instance;
    //是否测试
    public static final boolean isCs = true;
    //当前屏幕宽高
    public int screenW = 0;
    public int screenH = 0;
    public double lat = 0;
    public double lng = 0;
    public String screenWH;
    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;
    /**
     * 是否使用自动堆栈管理
     */
    private Activity mLastActivity = null;//最后一个显示的Activity
    private int mScreenWidth;//屏幕宽度
    private Vibrator mVibrator;

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    public boolean isLogin() {
        return this.isLogin;
    }

    public void setIsLogin(boolean isLogin, int i) {
        this.isLogin = isLogin;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isLogin = false;
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        instance = this;
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        // SDKInitializer.initialize(getApplicationContext());
        initImageLoader();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenW = dm.widthPixels;
        screenH = dm.heightPixels;
        mScreenWidth = dm.widthPixels;
        screenWH = screenH + "*" + screenW;
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("TAG"))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
        // 注册BroadcastReceiver
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN://App 的所有 UI 界面被隐藏，最常见的就是 App 被 home 键或者 back 键，置换到后台了
                break;
            case TRIM_MEMORY_RUNNING_MODERATE://表示 App 正常运行，并且不会被杀掉，但是目前手机内存已经有点低了，系统可能会根据 LRU List 来开始杀进程
                break;
            case TRIM_MEMORY_RUNNING_LOW://表示 App正常运行，并且不会被杀掉。但是目前手机内存已经非常低了。
                break;
            case TRIM_MEMORY_RUNNING_CRITICAL://表示 App 正在正常运行，但是系统已经开始根据 LRU List 的缓存规则杀掉了一部分缓存的进程。这个时候应该尽可能的释放掉不需要的内存资源，否者系统可能会继续杀掉其他缓存中的进程。
                break;
            case TRIM_MEMORY_BACKGROUND://表示 App 退出到后台，并且已经处于 LRU List 比较靠后的位置，暂时前面还有一些其他的 App 进程，暂时不用担心被杀掉
                break;
            case TRIM_MEMORY_COMPLETE://表示 App 退出到后台，并且已经处于 LRU List 比较考靠前的位置，并且手机内存已经极低，随时都有可能被系统杀掉。
                break;


        }

    }


    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getScreenWidth() {
        return this.mScreenWidth;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 取消BroadcastReceiver注册
    }


    /**
     * 退出登录需要关闭的数据
     */
    public void exitLogin() {
        MyApplication.getInstance().setIsLogin(false, 5);
    }


    /*
    最后一个显示的activity用于在任意地方弹出aialog
     */
    public void setLastActivity(Activity mLastActivity) {
        this.mLastActivity = mLastActivity;
    }

    public Activity getLastActivity() {
        return this.mLastActivity;
    }

    private void initImageLoader() {

    }

    public void getScreenSize() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0F;
        DIMEN_DPI = dm.densityDpi;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }
}
