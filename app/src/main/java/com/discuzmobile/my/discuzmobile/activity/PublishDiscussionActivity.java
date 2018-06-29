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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.discuzmobile.my.discuzmobile.R;
import com.discuzmobile.my.discuzmobile.bean.DiscussBean;
import com.discuzmobile.my.discuzmobile.bean.ResponseBean;
import com.discuzmobile.my.discuzmobile.widget.JsonUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishDiscussionActivity extends AppCompatActivity {

    @BindView(R.id.iv_pub_img)
    ImageView ivPubImg;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.sp_kind)
    Spinner spKind;
    @BindView(R.id.et_content)
    TextView etContent;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.tv_back)
    TextView tvBack;

    // 声明SharedPreference对象
    private SharedPreferences sp;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_discussion_layout);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_pub_img, R.id.tv_publish, R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pub_img:

                break;
            case R.id.tv_publish:
                Log.i("etTitle: ", etTitle.getText().toString());
                Log.i("spKind: ", spKind.getSelectedItem().toString());
                Log.i("etContent: ", etContent.getText().toString());
                // 获取种类id, 并异步发表文章
                getKindIdByName(spKind.getSelectedItem().toString());
                break;
            case R.id.tv_back:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    /**
     * 根据分类名称获取分类id
     *
     * @param name 分类名称
     */
    private void getKindIdByName(String name) {

        String url = "http://112.74.57.49:8080/discussion/kind/selectByName";

        OkHttpClient okHttpClient = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(
                        MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"),
                        "name=" + name))
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("getKindIdByName", "获取分类id失败: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    ResponseBean responseBean = JsonUtils.getObject(result, ResponseBean.class);
                    if (responseBean != null) {
                        if (100 == responseBean.getCode()) {
                            Log.i("getKindIdByName", "获取分类id成功: " + result);
                            JSONObject jsonObject = JSON.parseObject(responseBean.getBody().toString());
                            Object kindId = jsonObject.get("kindId");
                            Log.i("kindId.toString(): ", Long.valueOf(kindId.toString()) + "");
                            publish(Long.valueOf(kindId.toString()));
                            return;
                        }
                    }
                }
                Log.e("getKindIdByName", "获取分类id失败: " + response.message());
            }
        });
    }

    /**
     * 发表文章
     *
     * @param kindId 分类id
     */
    private void publish(Long kindId) {
        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        Long userId = sp.getLong("userId", 000L);
        String url = "http://112.74.57.49:8080/discussion/discuss/publishDiscuss";
        Map<String, Object> map = new HashMap<>();
        map.put("title", etTitle.getText().toString());
        map.put("kindId", kindId);
        map.put("userId", userId);
        map.put("discussion", etContent.getText().toString());

        OkHttpUtils
                .postString()
                .url(url)
                .content(new Gson().toJson(map))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("PUBLISH", "发表失败: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ResponseBean responseBean = JsonUtils.getObject(response, ResponseBean.class);

                        if (responseBean != null) {
                            if (100 == responseBean.getCode()) {
                                String body = responseBean.getBody().toString();
                                DiscussBean discussBean = new Gson().fromJson(body, DiscussBean.class);
                                Intent intent = new Intent(PublishDiscussionActivity.this, ForumListActivity.class);
                                Bundle data = new Bundle();
                                data.putString("kindName", spKind.getSelectedItem().toString());
                                data.putLong("kindId", discussBean.getKindId());
                                intent.putExtra("data", data);
                                startActivity(intent);
                                Log.i("PUBLISH", "发表成功: " + response);
                                return;
                            }
                        }
                        Log.e("PUBLISH", "发表失败: " + response);
                    }
                });
    }
}
