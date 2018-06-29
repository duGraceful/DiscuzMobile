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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.discuzmobile.my.discuzmobile.R;
import com.discuzmobile.my.discuzmobile.adapter.LeaveWordAdapter;
import com.discuzmobile.my.discuzmobile.bean.CommentsBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 论坛文章详情页面
 */
public class ForumDetailsActivity extends AppCompatActivity {

    private static final int DETAILS_CODE = 100;

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

    private List<CommentsBean> list = new ArrayList<>();
    private LeaveWordAdapter leaveWordAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forumdetails_layout);
        ButterKnife.bind(this);
        initData();
        //头像圆角
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        Bundle data = getIntent().getBundleExtra("data");
        tvKind.setText(data.getString("kind"));
        tvTitle.setText(data.getString("title"));
        tvDiscussion.setText(data.getString("discussion"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String sd = sdf.format(new Date(data.getLong("time")));
        etTime.setText(sd);

        Glide.with(this)
                .load(data.getString("image"))
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
                    String userName = getSharedPreferences("User", Context.MODE_PRIVATE).getString("userName", "未登录用户");
                    String imgUrl = getSharedPreferences("User", Context.MODE_PRIVATE).getString("headPicture", "http://suo.im/560IXb");

                    list.add(new CommentsBean(imgUrl, content, userName, "谢谢支持~~~"));
                    leaveWordAdapter.setBorrwList(list);
                }
            }
        }
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
}
