package com.discuzmobile.my.discuzmobile.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.discuzmobile.my.discuzmobile.R;
import com.discuzmobile.my.discuzmobile.bean.ResponseBean;
import com.discuzmobile.my.discuzmobile.widget.JsonUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 点击头像进入个人中心
 */
public class PersonalCenterActivity extends AppCompatActivity {

    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.iv_user_img)
    ImageView ivUserImg;
    @BindView(R.id.tv_attention)
    TextView tvAttention;
    @BindView(R.id.tv_private)
    TextView tvPrivate;
    @BindView(R.id.et_user_name)
    EditText etUserName;

    private String userName;
    private Boolean onClick = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center_layout);
        ButterKnife.bind(this);
        initData();

        ScrollView svPersonalCenter = (ScrollView) findViewById(R.id.sv_personal_center);
        svPersonalCenter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                etUserName.setFocusable(false);
                // 关闭输入法
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etUserName.getWindowToken(), 0);
                String newUserName = etUserName.getText().toString().trim();
                if (userName.equals(newUserName)) {
                    Log.i("ChangeUserName", "用户名字没有发生变化,不做修改!");
                } else {
                    Log.i("ChangeUserName", MessageFormat.format("原始名字: {0}, 修改后的名字: {1}", userName, newUserName));
                    userName = newUserName;
                    updateUserName(userName);
                }
                return false;
            }
        });
    }

    private void updateUserName(final String userName) {
        if (userName != null && userName.length() > 0) {
            Log.i("ChangeUserName", "调用后台, 修改用户名字! " + userName);

            String url = "http://112.74.57.49:8080/discussion/user/update";

            Long userId = getSharedPreferences("User", Context.MODE_PRIVATE).getLong("userId", -1000L);
            if (userId < 0) {
                Log.e("ChangeUserName", "修改用户名字失败, 原因UserId非法: " + userId);
                return;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("userName", userName);
            map.put("userId", userId);

            OkHttpUtils
                    .postString()
                    .url(url)
                    .content(new Gson().toJson(map))
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.e("ChangeUserName", "修改用户名字失败: " + e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            ResponseBean responseBean = JsonUtils.getObject(response, ResponseBean.class);
                            if (responseBean != null) {
                                if (100 == responseBean.getCode()) {
                                    Log.i("ChangeUserName", "修改用户名字成功: " + response);
                                    getSharedPreferences("User", Context.MODE_PRIVATE).edit().putString("userName", userName).apply();
                                    return;
                                }
                            }
                            Log.e("ChangeUserName", "修改用户名字失败: " + response);
                        }
                    });
        }
    }

    private void initData() {
        SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        userName = sp.getString("userName", "给自己起个名字吧");
        if (userName.trim().length() == 0) {
            userName = "给自己起个名字吧";
        }

        etUserName.setText(userName);
        // 默认输入框不可输入
        etUserName.setFocusableInTouchMode(onClick);

        String headPicture = sp.getString("headPicture", "http://112.74.57.49:88/img/1530271655236.png");
        if (headPicture.trim().length() == 0) {
            headPicture = "http://112.74.57.49:88/img/1530271655236.png";
        }
        Glide.with(this)
                .load(headPicture)
                .bitmapTransform(new RoundedCornersTransformation(this, 20, 0))
                .into(ivUserImg);
    }

    @OnClick({R.id.tv_back, R.id.tv_attention, R.id.tv_private, R.id.et_user_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                this.finish();
                break;
            case R.id.tv_attention:
                Toast.makeText(this, "关注", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_private:
                Toast.makeText(this, "私信", Toast.LENGTH_SHORT).show();
                break;
            case R.id.et_user_name:
                etUserName.setFocusableInTouchMode(!onClick);
                break;
        }
    }
}
