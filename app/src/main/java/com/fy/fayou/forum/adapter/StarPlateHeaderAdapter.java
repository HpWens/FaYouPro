package com.fy.fayou.forum.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.forum.bean.PlateEntity;
import com.fy.fayou.utils.GlideOption;
import com.meis.base.mei.adapter.MeiBaseAdapter;

public class StarPlateHeaderAdapter extends MeiBaseAdapter<PlateEntity> {

    OnItemListener mListener;

    public StarPlateHeaderAdapter(OnItemListener listener) {
        super(R.layout.item_star_plate_header);
        this.mListener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PlateEntity item) {
        helper.setText(R.id.tv_name, getNonEmpty(item.nickName));

        ImageView ivThumb = helper.getView(R.id.iv_thumb);
        Glide.with(mContext)
                .load(getNonEmpty(item.avatar))
                .apply(GlideOption.getItemCircleOption(50, 50))
                .into(ivThumb);

        TextView tvFollow = helper.getView(R.id.tv_follow);
        tvFollow.setBackgroundResource(item.followed ? R.drawable.my_unfollow_round : R.drawable.my_follow_red_round);
        tvFollow.setTextColor(helper.itemView.getResources().getColor(item.followed ? R.color.color_d2d2d2 : R.color.color_ffffff));
        tvFollow.setText(item.followed ? "已关注" : "+关注");

        helper.itemView.setOnClickListener(v -> {
            mListener.onItem(v, item);
        });

        tvFollow.setOnClickListener(v -> {
            mListener.onFollow(v, helper.getAdapterPosition(), item);
        });

    }

    public interface OnItemListener {
        void onFollow(View view, int position, PlateEntity item);

        void onItem(View view, PlateEntity item);
    }

}
