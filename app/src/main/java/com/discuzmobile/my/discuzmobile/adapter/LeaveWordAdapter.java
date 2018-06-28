package com.discuzmobile.my.discuzmobile.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.discuzmobile.my.discuzmobile.R;
import com.discuzmobile.my.discuzmobile.app.RecyclerItemOnClickListener;
import com.discuzmobile.my.discuzmobile.bean.CommentsBean;
import com.discuzmobile.my.discuzmobile.bean.ListBean;

import org.w3c.dom.Text;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class LeaveWordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<CommentsBean> mLists;
    private RecyclerItemOnClickListener mRecyclerItemOnClickListener;

    public LeaveWordAdapter(Context mContext, List<CommentsBean> mLists) {
        this.mContext = mContext;
        this.mLists = mLists;
    }

    /**
     * 设置监听
     *
     * @param
     */
    public void setOnItemLinener(RecyclerItemOnClickListener mRecyclerItemOnClickListener) {
        this.mRecyclerItemOnClickListener = mRecyclerItemOnClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHold mVocationHolder = new ViewHold(LayoutInflater.from(mContext).inflate(R.layout.item_leaveword_layout, parent, false));

        return mVocationHolder;
    }

    /**
     * @param mLists
     */
    public void setBorrwList(List<CommentsBean> mLists) {
        this.mLists = mLists;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHold mholder = (ViewHold) holder;
        ViewGroup.LayoutParams lp = mholder.ll_par.getLayoutParams();
        lp.height = mholder.ll_par.getLayoutParams().height;
        mholder.ll_par.setLayoutParams(lp);
        ViewHold mVocationHolder = ((ViewHold) holder);

        Glide.with(mContext)
                .load(mLists.get(position).getUserImgUrl())
                .bitmapTransform(new RoundedCornersTransformation(mContext, 20, 0))
                .into(mVocationHolder.iv_user_img);
        mVocationHolder.tv_user_name.setText(mLists.get(position).getUsername());
        mVocationHolder.tv_user_content.setText(mLists.get(position).getContent());
        if (TextUtils.isEmpty(mLists.get(position).getReplyUser())) {
            mVocationHolder.rl_writer.setVisibility(View.GONE);
        } else {
            mVocationHolder.rl_writer.setVisibility(View.VISIBLE);
        }
        mVocationHolder.tv_writer_content.setText(mLists.get(position).getReplyUser());
        mVocationHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecyclerItemOnClickListener != null) {
                    mRecyclerItemOnClickListener.OnItemClickLinstener(null, position, mLists.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    private class ViewHold extends RecyclerView.ViewHolder {

        public LinearLayout ll_par;
        public ImageView iv_user_img;
        public TextView tv_user_name;
        public TextView tv_user_content;
        public TextView tv_writer_content;
        public RelativeLayout rl_writer;


        public ViewHold(View itemView) {
            super(itemView);
            ll_par = (LinearLayout) itemView.findViewById(R.id.ll_par);
            iv_user_img = (ImageView) itemView.findViewById(R.id.iv_user_img);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            tv_user_content = (TextView) itemView.findViewById(R.id.tv_user_content);
            tv_writer_content = (TextView) itemView.findViewById(R.id.tv_writer_content);
            rl_writer = (RelativeLayout) itemView.findViewById(R.id.rl_writer);
        }
    }
}
