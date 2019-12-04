package com.fy.fayou.forum.adapter;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.forum.bean.PlateEntity;
import com.fy.fayou.utils.GlideOption;
import com.meis.base.mei.adapter.MeiBaseAdapter;

public class PlateAdapter extends MeiBaseAdapter<PlateEntity> {

    public PlateAdapter() {
        super(R.layout.item_forum_home_plate);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PlateEntity item) {

        helper.setText(R.id.tv_name, getNonEmpty(item.name));

        ImageView ivThumb = helper.getView(R.id.iv_thumb);
        Glide.with(mContext)
                .load(item.helperIsMy ? R.mipmap.forum_my_plate : getNonEmpty(item.logo))
                .apply(GlideOption.getItemCircleOption(70, 70))
                .into(ivThumb);

        helper.itemView.setOnClickListener(v -> {
            if (item.helperIsMy) {
                ARoute.jumpForumFollow();
            } else {
                ARoute.jumpPlateList(item.id);
            }
        });
    }
}
