package com.fy.fayou.my.adapter;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.bean.UserInfo;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.utils.GlideOption;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.vondear.rxtool.RxImageTool;

import java.util.ArrayList;

public class FollowAdapter extends MeiBaseAdapter<UserInfo> {

    private int width;
    private int height;

    private OnItemListener mListener;

    public FollowAdapter(OnItemListener listener) {
        super(R.layout.item_my_follow, new ArrayList<>());
        width = height = RxImageTool.dip2px(40);
        this.mListener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, final UserInfo item) {
        helper.setText(R.id.tv_followed, item.follow ? "互相关注" : "已关注")
                .setText(R.id.tv_name, item.nickName == null ? "" : item.nickName)
                .setVisible(R.id.tv_follow, item.isCancelFollow)
                .setVisible(R.id.tv_followed, !item.isCancelFollow);

        helper.getView(R.id.tv_followed).setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onFollow(item, true, helper.getAdapterPosition());
            }
        });

        helper.getView(R.id.tv_follow).setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onFollow(item, false, helper.getAdapterPosition());
            }
        });

        ImageView ivAvatar = helper.getView(R.id.iv_avatar);
        Glide.with(helper.itemView.getContext())
                .load(item.avatar == null ? "" : item.avatar)
                .apply(GlideOption.getAvatarOption(width, height))
                .into(ivAvatar);

        helper.getView(R.id.tv_name).setOnClickListener(v -> {
            ARoute.jumpUserCenter(item.id);
        });
        ivAvatar.setOnClickListener(v -> {
            ARoute.jumpUserCenter(item.id);
        });
    }

    public interface OnItemListener {
        void onFollow(UserInfo user, boolean cancelFollow, int position);
    }
}
