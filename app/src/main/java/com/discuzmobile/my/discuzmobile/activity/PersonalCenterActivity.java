package com.discuzmobile.my.discuzmobile.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.discuzmobile.my.discuzmobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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
    @BindView(R.id.tv_user_name)
    TextView tvUserName;

    private SharedPreferences sp;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center_layout);
        ButterKnife.bind(this);
        initData();
        //头像圆角

    }

    private void initData() {
        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        tvUserName.setText(sp.getString("userName","给自己起个名字吧"));
        Glide.with(this)
                .load(sp.getString("headPicture",R.mipmap.user_icon+""))
                .bitmapTransform(new RoundedCornersTransformation(this, 20, 0))
                .into(ivUserImg);
    }


    @OnClick({R.id.tv_back, R.id.tv_attention, R.id.tv_private})
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
        }
    }
}
