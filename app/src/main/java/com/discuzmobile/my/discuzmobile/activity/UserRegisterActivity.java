package com.discuzmobile.my.discuzmobile.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.discuzmobile.my.discuzmobile.R;
import com.discuzmobile.my.discuzmobile.bean.ResponseBean;
import com.discuzmobile.my.discuzmobile.bean.UserBean;
import com.discuzmobile.my.discuzmobile.widget.JsonUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;

public class UserRegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_toast)
    TextView tvToast;

    private SharedPreferences sp;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_layout);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_register, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_register:
                Toast.makeText(this, "注册", Toast.LENGTH_SHORT).show();
                String url = "http://112.74.57.49:8080/discussion/user/register";

                Map<String, String> map = new HashMap<>();
                map.put("email", etEmail.getText().toString());
                map.put("password", etPassword.getText().toString());
                map.put("userName", etUsername.getText().toString());

                OkHttpUtils
                        .postString()
                        .url(url)
                        .content(new Gson().toJson(map))
                        .mediaType(MediaType.parse("application/json; charset=utf-8"))
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.e("LOGIN", "注册失败: " + e.getMessage());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                ResponseBean responseBean = JsonUtils.getObject(response, ResponseBean.class);
                                Log.i("LOGIN", "注册成功xznzm: " + responseBean.toString());
                                if (responseBean != null) {
                                    if (100 == responseBean.getCode()) {
                                        Log.i("LOGIN", "注册成功: " + response);
                                        String body = responseBean.getBody().toString();

                                        UserBean userBean = new Gson().fromJson(body, UserBean.class);//将json字符串转换为json对象

                                        Log.i("shuyu用户",userBean.toString());

                                        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor edit = sp.edit();
                                        //通过editor对象写入数据
                                        edit.putLong("userId", userBean.getUserId());
                                        edit.putString("email", userBean.getEmail());
                                        edit.putString("userName", userBean.getUserName());
                                        edit.putString("headPicture", userBean.getHeadPicture());
                                        //提交数据存入到xml文件中
                                        edit.commit();

                                        Intent intent = new Intent(UserRegisterActivity.this, MainActivity.class);
                                        // 跳转首页
                                        startActivity(intent);
                                        return;
                                    } else {
                                        final String msg = responseBean.getMsg();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tvToast.setText("注册失败: " + msg);
                                            }
                                        });
                                    }
                                }
                                Log.e("LOGIN", "注册失败: " + response);
                            }
                        });

                break;

            case R.id.tv_login:
                startActivity(new Intent(this, UserLoginActivity.class));
                break;
        }
    }
}
