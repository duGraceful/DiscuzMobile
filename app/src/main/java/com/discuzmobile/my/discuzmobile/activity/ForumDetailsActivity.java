package com.discuzmobile.my.discuzmobile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.discuzmobile.my.discuzmobile.R;
import com.discuzmobile.my.discuzmobile.adapter.LeaveWordAdapter;
import com.discuzmobile.my.discuzmobile.bean.CommentsBean;
import com.discuzmobile.my.discuzmobile.bean.ResponseBean;
import com.discuzmobile.my.discuzmobile.utils.ApplicationToastUtil;
import com.discuzmobile.my.discuzmobile.widget.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 论坛文章详情页面
 */
public class ForumDetailsActivity extends AppCompatActivity {

    protected static final int DETAILS_CODE = 100;

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_kind)
    TextView tvKind;
    @BindView(R.id.et_title)
    TextView tvTitle;
    @BindView(R.id.et_time)
    TextView etTime;
    @BindView(R.id.tv_discussion)
    TextView tvDiscussion;
    @BindView(R.id.my_recyclerview)
    RecyclerView myRecyclerView;
    @BindView(R.id.nestedscrollview)
    NestedScrollView nestedscrollview;
    @BindView(R.id.tv_leave)
    TextView tvLeave;

    private static Bundle bundlePrevious;

    private List<CommentsBean> list = new ArrayList<>();
    private LeaveWordAdapter leaveWordAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forumdetails_layout);
        ButterKnife.bind(this);

        bundlePrevious = getIntent().getBundleExtra("data");
        initData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        getComment();

        tvKind.setText(bundlePrevious.getString("kind"));
        tvTitle.setText(bundlePrevious.getString("title"));
        tvDiscussion.setText(bundlePrevious.getString("discussion"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String sd = sdf.format(new Date(bundlePrevious.getLong("time")));
        etTime.setText(sd);

        Glide.with(this)
                .load(bundlePrevious.getString("image"))
                .bitmapTransform(new RoundedCornersTransformation(this, 20, 0))
                .into(ivImage);

        nestedscrollview.smoothScrollTo(0, 20);

        leaveWordAdapter = new LeaveWordAdapter(this, list);
        if (myRecyclerView instanceof RecyclerView) {
            myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            myRecyclerView.setAdapter(leaveWordAdapter);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DETAILS_CODE) {
            if (data != null) {
                if (!TextUtils.isEmpty(data.getStringExtra("content"))) {
                    String content = data.getStringExtra("content");
                    String userName = getSharedPreferences("User", Context.MODE_PRIVATE).getString("userName", "未知用户");
                    String imgUrl = getSharedPreferences("User", Context.MODE_PRIVATE).getString("headPicture", "http://112.74.57.49:88/img/1530261961347.jpg");
                    publishComment(content);

                    list.add(new CommentsBean(imgUrl, content, userName, null));

                    leaveWordAdapter.setBorrwList(list);
                }
            }
        }
    }

    private void publishComment(String content) {
        String url = "http://112.74.57.49:8080/discussion/comment/publishComment";

        Map<String, Object> map = new HashMap<>();

        map.put("discuzId", bundlePrevious.getLong("discussId"));
        map.put("userId", getSharedPreferences("User", Context.MODE_PRIVATE).getLong("userId", 0L));
        map.put("content", content);

        OkHttpUtils
                .postString()
                .url(url)
                .content(new Gson().toJson(map))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("COMMENT", "评论失败: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ResponseBean responseBean = JsonUtils.getObject(response, ResponseBean.class);
                        if (responseBean != null) {
                            if (100 == responseBean.getCode()) {
                                Log.i("COMMENT", "评论成功: " + response);
                                // 主线程更新UI
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ApplicationToastUtil.showToastSimple("评论成功!");
                                        leaveWordAdapter.notifyDataSetChanged();
                                    }
                                });
                                return;
                            }
                        }
                        Log.e("COMMENT", "评论失败: " + response);
                    }
                });
    }

    private void getComment() {
        final String url = "http://112.74.57.49:8080/discussion/comment/selectByDiscuss";

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("discussId", String.valueOf(bundlePrevious.getLong("discussId")))
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("GET_COMMENT", "获取之前的评论: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Log.i("GET_COMMENT", "获取之前的评论: " + result);
                    ResponseBean responseBean = JsonUtils.getObject(result, ResponseBean.class);
                    if (responseBean != null) {
                        if (100 == responseBean.getCode() && responseBean.getBody() != null) {
                            String body = responseBean.getBody().toString();
                            // 将json字符串转换为json对象
                            List<CommentEntity> discussBeanList = new Gson().fromJson(body,
                                    new TypeToken<List<CommentEntity>>() {
                                    }.getType());
                            for (CommentEntity commentEntity : discussBeanList) {
                                list.add(new CommentsBean(
                                        commentEntity.getHeadPicture() == null ? "http://112.74.57.49:88/img/1530261961347.jpg" : commentEntity.getHeadPicture(),
                                        commentEntity.getContent(),
                                        commentEntity.getUserName() == null ? "未知用户" : commentEntity.getUserName(),
                                        commentEntity.getReplyUser()
                                ));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    leaveWordAdapter.notifyDataSetChanged();
                                }
                            });
                            return;
                        }
                    }
                }
                Log.e("GET_COMMENT", "获取之前的评论: " + response.message());
            }
        });
    }

    @OnClick({R.id.tv_back, R.id.tv_kind, R.id.tv_leave})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                this.finish();
                break;
            case R.id.tv_kind:
                Toast.makeText(this, tvTitle.getText(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_leave:
                Intent intent = new Intent(new Intent(this, LeaveActivity.class));
                Bundle data1 = getIntent().getBundleExtra("data");
                intent.putExtra("title", data1.getString("title"));
                startActivityForResult(intent, DETAILS_CODE);
                break;
        }
    }

    private class CommentEntity {
        /**
         * 评论信息
         */
        private Long commentId;
        private Long discuzId;
        private Long userId;
        private String content;
        private String replyUser;

        /**
         * 用户信息
         */
        private String email;
        private String userName;
        private String headPicture;

        public Long getCommentId() {
            return commentId;
        }

        public void setCommentId(Long commentId) {
            this.commentId = commentId;
        }

        public Long getDiscuzId() {
            return discuzId;
        }

        public void setDiscuzId(Long discuzId) {
            this.discuzId = discuzId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getReplyUser() {
            return replyUser;
        }

        public void setReplyUser(String replyUser) {
            this.replyUser = replyUser;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getHeadPicture() {
            return headPicture;
        }

        public void setHeadPicture(String headPicture) {
            this.headPicture = headPicture;
        }
    }
}
