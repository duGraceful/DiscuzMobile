package com.discuzmobile.my.discuzmobile.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.discuzmobile.my.discuzmobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.discuzmobile.my.discuzmobile.activity.ForumDetailsActivity.DETAILS_CODE;

/**
 * 留言页面
 */
public class LeaveActivity extends AppCompatActivity {

    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_kind)
    TextView tvTitle;
    @BindView(R.id.ed_content)
    EditText edContent;
    @BindView(R.id.tv_bt)
    TextView tvBt;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_layout);
        ButterKnife.bind(this);
        initData();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
            tvTitle.setText(getIntent().getStringExtra("title"));
        }
    }

    @OnClick({R.id.tv_back, R.id.tv_kind, R.id.tv_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_back:
                finish();
                break;

            case R.id.tv_kind:
                break;

            case R.id.tv_bt:
                if (TextUtils.isEmpty(edContent.getText().toString().trim())) {
                    Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("content", edContent.getText().toString().trim());
                setResult(DETAILS_CODE, intent);
                finish();
                break;
        }
    }
}
