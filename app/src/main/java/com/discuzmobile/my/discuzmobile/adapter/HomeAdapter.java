package com.discuzmobile.my.discuzmobile.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.discuzmobile.my.discuzmobile.bean.ListBean;
import com.discuzmobile.my.discuzmobile.R;
import com.discuzmobile.my.discuzmobile.app.RecyclerItemOnClickListener;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.support.design.R.styleable.RecyclerView;

/**
 * Created by
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ListBean> mLists;
    private RecyclerItemOnClickListener mRecyclerItemOnClickListener;

    public HomeAdapter(Context mContext, List<ListBean> mLists) {
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
        ViewHold mVocationHolder = new ViewHold(LayoutInflater.from(mContext).inflate(R.layout.item_home_layout, parent, false));

        return mVocationHolder;
    }

    /**
     * @param mLists
     */
    public void setBorrwList(List<ListBean> mLists) {
        this.mLists = mLists;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHold mholder = (ViewHold) holder;
        ViewGroup.LayoutParams lp = mholder.ll_par.getLayoutParams();
        lp.height = mholder.item_bg.getLayoutParams().height;
        mholder.ll_par.setLayoutParams(lp);
        ViewHold mVocationHolder = ((ViewHold) holder);
        mVocationHolder.itemname.setText(mLists.get(position).getName());

        Glide.with(mContext)
                .load(mLists.get(position).getUrl())
                .bitmapTransform(new RoundedCornersTransformation(mContext, 20, 0))
                .into(mVocationHolder.iv_imt_bg);

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

        public TextView itemname;
        public FrameLayout item_bg;
        public LinearLayout ll_par;
        public ImageView iv_imt_bg;

        public TextView tv_message_content;


        public ViewHold(View itemView) {
            super(itemView);
            itemname = (TextView) itemView.findViewById(R.id.item_name);
            item_bg = (FrameLayout) itemView.findViewById(R.id.item_img_bg);
            ll_par = (LinearLayout) itemView.findViewById(R.id.ll_par);
            iv_imt_bg = (ImageView) itemView.findViewById(R.id.iv_imt_bg);
        }
    }
}
