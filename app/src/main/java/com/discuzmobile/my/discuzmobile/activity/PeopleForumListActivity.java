package com.discuzmobile.my.discuzmobile.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.discuzmobile.my.discuzmobile.R;
import com.discuzmobile.my.discuzmobile.adapter.ForumListAdapter;
import com.discuzmobile.my.discuzmobile.app.RecyclerItemOnClickListener;
import com.discuzmobile.my.discuzmobile.bean.DiscussBean;
import com.discuzmobile.my.discuzmobile.bean.ListBean;
import com.discuzmobile.my.discuzmobile.bean.ResponseBean;
import com.discuzmobile.my.discuzmobile.widget.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

/**
 * 论坛List页面
 */
public class PeopleForumListActivity extends AppCompatActivity implements RecyclerItemOnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.my_recyclerview)
    RecyclerView myRecyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    private ArrayList<ListBean> list;
    private String title;
    private SharedPreferences sp;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_forum_list_main);
        ButterKnife.bind(this);
        initData();
        swiperefreshlayout.setOnRefreshListener(this);
        // navView.setNavigationItemSelectedListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        list = new ArrayList<>();
        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        // 获取分类数据
        getKindsData();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ForumListAdapter homeAdapter = new ForumListAdapter(this, list);
        if (myRecyclerView instanceof RecyclerView) {
            myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
            myRecyclerView.setLayoutManager(mGridLayoutManager);
            myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            myRecyclerView.setAdapter(homeAdapter);
        }
        homeAdapter.setOnItemLinener(this);
    }

    private void getKindsData() {
        Log.i("文件", sp.toString());
        title = sp.getString("userName", "个人论坛列表");
        tvTitle.setText(title);

        String url = "http://112.74.57.49:8080/discussion/discuss/selectByUserId";
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id", sp.getLong("userId", 0L) + "")
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("GET_DISCUSS_BY_KIND", "获取文章: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Log.i("GET_DISCUSS_BY_KIND", "获取文章: " + result);
                    ResponseBean responseBean = JsonUtils.getObject(result, ResponseBean.class);
                    if (responseBean != null) {
                        if (100 == responseBean.getCode()) {
                            String body = responseBean.getBody().toString();

                            List<DiscussBean> discussBeanList = new Gson().fromJson(body,
                                    new TypeToken<List<DiscussBean>>() {
                                    }.getType());

                            for (DiscussBean discussBean : discussBeanList) {
                                list.add(new ListBean(discussBean.getDiscuzId(), discussBean.getTitle(),
                                        discussBean.getImage(), discussBean.getReportTime(), discussBean.getDiscussion()));
                            }

                        }
                    }
                }
            }
        });
    }

    /**
     * item监听
     */
    @Override
    public void OnItemClickListener(View view, int position, Object obj) {
        ListBean listBean = (ListBean) obj;
        Bundle dataTemp = getIntent().getBundleExtra("data");
        Intent intent = new Intent(PeopleForumListActivity.this, ForumDetailsActivity.class);
        Bundle data = new Bundle();
        data.putLong("discussId", listBean.getId());
        data.putString("discussion", listBean.getDiscussion());
        data.putString("title", listBean.getName());
        data.putString("image", listBean.getUrl());
        data.putLong("time", listBean.getTime());
        data.putString("kind", dataTemp.getString(""));
        data.putLong("userID", sp.getLong("userId", 0L));
        intent.putExtra("data", data);

        startActivity(intent);
    }

    @OnClick({R.id.tv_back, R.id.my_recyclerview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                this.finish();
                break;
            case R.id.my_recyclerview:
                break;
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
                swiperefreshlayout.setRefreshing(false);
            }
        }, 2000);
    }

}
