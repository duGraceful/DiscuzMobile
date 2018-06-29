package com.discuzmobile.my.discuzmobile.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.discuzmobile.my.discuzmobile.R;
import com.discuzmobile.my.discuzmobile.app.SharedInfo;

/**
 * 应用启动界面
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_start);
        initData();
    }

    protected void initData() {
        new Thread(new MyThread()).start();
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                Intent intent;
//                Bundle data = getIntent().getBundleExtra("data");
//                Long id = data.getLong("userId");

                if (SharedInfo.getInstance().getLastLogin()) {
                    intent = new Intent(LaunchActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(LaunchActivity.this, GuiDanceActivity.class);
                }
                startActivity(intent);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
