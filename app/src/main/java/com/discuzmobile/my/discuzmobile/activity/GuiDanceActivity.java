package com.discuzmobile.my.discuzmobile.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.discuzmobile.my.discuzmobile.R;
import com.discuzmobile.my.discuzmobile.adapter.GuidePagerAdapter;
import com.discuzmobile.my.discuzmobile.app.SharedInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GuiDanceActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    public static final String LAST_LOGN = "last_login";

    private ViewPager vp;
    private int[] imageIdArray;//图片资源的数组
    private List<View> viewList;//图片资源的集合
    private ViewGroup vg;//放置圆点

    //实例化原点View
    private ImageView iv_point;
    private ImageView[] ivPointArray;

    //最后一页的按钮
    private Button ib_start;

    private SharedPreferences sp ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance_layout);
        initData();
        initViewListener();
    }

    protected void initViewListener() {

        ib_start.setOnClickListener(this);
    }

    protected void initData() {
        ib_start = (Button) findViewById(R.id.guide_ib_start);

        initViewPager();
        //加载底部圆点
        initPoint();
    }

    private void initPoint() {
        //这里实例化LinearLayout
        vg = (ViewGroup) findViewById(R.id.guide_ll_point);
        //根据ViewPager的item数量实例化数组
        ivPointArray = new ImageView[viewList.size()];
        //循环新建底部圆点ImageView，将生成的ImageView保存到数组中
        int size = viewList.size();
        for (int i = 0; i < size; i++) {
            iv_point = new ImageView(this);
            iv_point.setLayoutParams(new ViewGroup.LayoutParams(40, 40));
            iv_point.setPadding(30, 0, 30, 0);//left,top,right,bottom
            ivPointArray[i] = iv_point;
            //第一个页面需要设置为选中状态，这里采用两张不同的图片
            if (i == 0) {
                iv_point.setBackgroundResource(R.mipmap.ps2);
            } else {
                iv_point.setBackgroundResource(R.mipmap.ps1);
            }
            //将数组中的ImageView加入到ViewGroup
            vg.addView(ivPointArray[i]);
        }
    }

    private void initViewPager() {
        vp = (ViewPager) findViewById(R.id.guide_vp);
        //实例化图片资源
        imageIdArray = new int[]{R.mipmap.img1, R.mipmap.img2, R.mipmap.img3, R.mipmap.img4};
        viewList = new ArrayList<>();
        //获取一个Layout参数，设置为全屏
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        //循环创建View并加入到集合中
        int len = imageIdArray.length;
        for (int i = 0; i < len; i++) {
            //new ImageView并设置全屏和图片资源
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(imageIdArray[i]);

            //将ImageView加入到集合中
            viewList.add(imageView);
        }

        //View集合初始化好后，设置Adapter
        vp.setAdapter(new GuidePagerAdapter(viewList));
        //设置滑动监听
        vp.setOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int length = imageIdArray.length;
        for (int i = 0; i < length; i++) {
            ivPointArray[position].setBackgroundResource(R.mipmap.ps2);
            if (position != i) {
                ivPointArray[i].setBackgroundResource(R.mipmap.ps1);
            }
        }

        //判断是否是最后一页，若是则显示按钮
        if (position == imageIdArray.length - 1) {
            ib_start.setVisibility(View.VISIBLE);
        } else {
            ib_start.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        SharedInfo.getInstance().putLastLogin(true);
        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        Long userId = sp.getLong("userId",000L);
        if(userId == 000L){
            startActivity(new Intent(this, UserLoginActivity.class));
        }else{
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
