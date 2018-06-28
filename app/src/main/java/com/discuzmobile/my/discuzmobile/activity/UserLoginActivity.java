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

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserLoginActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_user_login_layout);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_login, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                Toast.makeText(this, "登录", Toast.LENGTH_SHORT).show();
                final String url = "http://112.74.57.49:8080/discussion/user/login";

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("email", etEmail.getText().toString())
                        .add("password", etPassword.getText().toString())
                        .build();

                final Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("LOGIN", "登录失败: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            Log.i("LOGIN", "登录成功: " + result);
                            ResponseBean responseBean = JsonUtils.getObject(result, ResponseBean.class);
                            if (responseBean != null) {
                                if (100 == responseBean.getCode()) {
                                    String body = responseBean.getBody().toString();
                                    UserBean userBean = new Gson().fromJson(body, UserBean.class);//将json字符串转换为json对象
                                    Log.i("登录用户aaa", userBean.toString());


                                    sp = getSharedPreferences("User", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edit = sp.edit();
                                    //通过editor对象写入数据
                                    edit.putLong("userId", userBean.getUserId());
                                    edit.putString("email", userBean.getEmail());
                                    edit.putString("userName", userBean.getUserName());
                                    edit.putString("headPicture", userBean.getHeadPicture());
                                    //提交数据存入到xml文件中
                                    edit.commit();


                                    // 跳转首页
                                    Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    return;
                                } else {
                                    final String msg = responseBean.getMsg();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvToast.setText("登录失败: " + msg);
                                        }
                                    });
                                }
                            }
                        }
                        Log.e("LOGIN", "登录失败: " + response.message());
                    }
                });
                break;

            case R.id.tv_register:
                startActivity(new Intent(this, UserRegisterActivity.class));
                break;
        }
    }
}
