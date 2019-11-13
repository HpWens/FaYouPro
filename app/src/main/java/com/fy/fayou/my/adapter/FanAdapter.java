package com.fy.fayou.my.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.bean.UserInfo;
import com.fy.fayou.utils.GlideOption;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.vondear.rxtool.RxImageTool;

import java.util.ArrayList;

public class FanAdapter extends MeiBaseAdapter<UserInfo> {

    private int width;
    private int height;

    private OnItemListener mListener;

    public FanAdapter(OnItemListener listener) {
        super(R.layout.item_my_fan, new ArrayList<>());
        width = height = RxImageTool.dip2px(40);
        this.mListener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, UserInfo item) {
        helper.setText(R.id.tv_name, item.nickName == null ? "" : item.nickName)
                .setVisible(R.id.tv_follow, !item.follow)
                .setVisible(R.id.tv_each_follow, item.follow);

        helper.getView(R.id.tv_follow).setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onFollow(item, helper.getAdapterPosition());
            }
        });

        ImageView ivAvatar = helper.getView(R.id.iv_avatar);
        Glide.with(helper.itemView.getContext())
                .load(item.avatar == null ? "" : item.avatar)
                .apply(GlideOption.getAvatarOption(width, height))
                .into(ivAvatar);

        ivAvatar.setOnClickListener(v -> {
            mListener.onJumpUserCenter(v, item.id);
        });

    }

    public interface OnItemListener {
        void onFollow(UserInfo user, int position);

        void onJumpUserCenter(View v, String id);
    }
}