package com.discuzmobile.my.discuzmobile.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.discuzmobile.my.discuzmobile.R;
import com.discuzmobile.my.discuzmobile.adapter.HomeAdapter;
import com.discuzmobile.my.discuzmobile.app.RecyclerItemOnClickListener;
import com.discuzmobile.my.discuzmobile.bean.KindBean;
import com.discuzmobile.my.discuzmobile.bean.ListBean;
import com.discuzmobile.my.discuzmobile.bean.ResponseBean;
import com.discuzmobile.my.discuzmobile.widget.CircleImageView;
import com.discuzmobile.my.discuzmobile.widget.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 主 Activity
 */
public class MainActivity extends AppCompatActivity implements RecyclerItemOnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.my_recyclerview)
    RecyclerView myRecyclerView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.iv_user_img)
    CircleImageView ivUserImg;
    @BindView(R.id.iv_user_name)
    TextView ivUserName;
    @BindView(R.id.iv_user_mail)
    TextView ivUserMail;
    @BindView(R.id.iv_user_personage)
    TextView ivUserPersonage;
    @BindView(R.id.iv_user_square)
    TextView ivUserSquare;
    @BindView(R.id.iv_user_attention)
    TextView ivUserAttention;
    @BindView(R.id.iv_user_setting)
    TextView ivUserSetting;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.nav_view)
    RelativeLayout navView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    private HomeAdapter homeAdapter;
    private static List<ListBean> list;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();

        // navView.setNavigationItemSelectedListener(this);
        swiperefreshlayout.setOnRefreshListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        list = new ArrayList<>();
        // 获取分类数据
        getKindsData();

        SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        ivUserName.setText(sp.getString("userName", "给自己起个名字吧"));
        ivUserMail.setText(sp.getString("email", "....."));

        String headPicture = sp.getString("headPicture", "http://112.74.57.49:88/img/1530271655236.png");
        if (headPicture.trim().length() == 0) {
            headPicture = "http://112.74.57.49:88/img/1530271655236.png";
        }

        Glide.with(this)
                .load(headPicture)
                .bitmapTransform(new RoundedCornersTransformation(this, 20, 0))
                .into(ivUserImg);

        homeAdapter = new HomeAdapter(this, list);
        if (myRecyclerView instanceof RecyclerView) {
            myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            myRecyclerView.setAdapter(homeAdapter);
        }
        homeAdapter.setOnItemLinener(this);
    }

    private void getKindsData() {
        String url = "http://112.74.57.49:8080/discussion/kind/selectAll";
        OkHttpUtils
                .postString()
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content("hello=123")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("LOGIN", "获取分类失败: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ResponseBean responseBean = JsonUtils.getObject(response, ResponseBean.class);

                        if (responseBean != null && 100 == responseBean.getCode()) {
                            String body = responseBean.getBody().toString();

                            List<KindBean> kinds = new Gson().fromJson(body, new TypeToken<List<KindBean>>() {
                            }.getType());

                            for (KindBean kind : kinds) {
                                list.add(new ListBean(kind.getKindId(), kind.getKindName(),
                                        kind.getKindImage()));
//                                SharedPreferences sp = getSharedPreferences("kind", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor edit = sp.edit();
//                                //通过editor对象写入数据
//                                edit.putLong("kindId", kind.getKindId());
//                                edit.putString("kindName", kind.getKindName());
//                                edit.putString("kindImage", kind.getKindImage());
//                                //提交数据存入到xml文件中
//                                edit.commit();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    homeAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
    }

    /**
     * 监听back键判断侧滑菜单是否打开。
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * item监听
     */
    @Override
    public void OnItemClickListener(View view, int position, Object obj) {
        ListBean listBean = (ListBean) obj;
        Intent intent = new Intent(new Intent(this, ForumListActivity.class));
        Bundle data = new Bundle();
        data.putString("title", listBean.getName());
        data.putLong("kindId", listBean.getId());
        intent.putExtra("data", data);
        Log.i(listBean.getName() + "类", JSON.toJSONString(listBean));
        startActivity(intent);
    }

    @OnClick({R.id.iv_user_img, R.id.iv_user_personage, R.id.iv_user_square, R.id.iv_user_attention, R.id.iv_user_setting, R.id.tv_exit, R.id.tv_menu, R.id.tv_edit, R.id.my_recyclerview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.tv_edit:
                Intent intent1 = new Intent(this, PublishDiscussionActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_user_img:
                Intent intent = new Intent(new Intent(this, PersonalCenterActivity.class));
                startActivity(intent);
                break;
            case R.id.iv_user_personage:
                Toast.makeText(this, "个人", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, PeopleForumListActivity.class));
                break;
            case R.id.iv_user_square:
                Toast.makeText(this, "广场", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.iv_user_attention:
                Toast.makeText(this, "关注", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_user_setting:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_exit:
                Toast.makeText(this, "退出", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, UserLoginActivity.class));
                break;
        }
    }

    @Override
    public void onRefresh() {
        // Android 数据不是双向绑定的, 有些数据需要刷新重新载入
        SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        ivUserName.setText(sp.getString("userName", "给自己起个名字吧"));

        new Handler().postDelayed(new Runnable() {
            public void run() {
                swiperefreshlayout.setRefreshing(false);
            }
        }, 2000);
    }

}
