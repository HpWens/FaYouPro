package com.fy.fayou.my.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.my.bean.CollectEntity;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.KtTimeUtils;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class CollectAdapter extends MeiBaseAdapter<CollectEntity> {

    private boolean isCollect;

    public CollectAdapter(boolean isCollect) {
        super(R.layout.item_my_collect, new ArrayList<>());
        this.isCollect = isCollect;
    }

    @Override
    public void convert(BaseViewHolder holder, CollectEntity item) {
        ImageView ivCover = holder.getView(R.id.iv_cover);

        Glide.with(holder.itemView.getContext())
                .load(getNonEmpty(item.cover))
                .apply(GlideOption.getItemOption(75, 75))
                .thumbnail(0.5f)
                .into(ivCover);

        ivCover.setVisibility(TextUtils.isEmpty(item.cover) ? View.GONE : View.VISIBLE);

        holder.setText(R.id.tv_title, getNonEmpty(item.title))
                .setText(R.id.tv_time, KtTimeUtils.INSTANCE.getYMDTime(isCollect ? item.createTime : item.businessTime));

        holder.itemView.setOnClickListener(v -> {
            ARoute.jumpDetail(TextUtils.isEmpty(item.browseRecordType) ? item.collectType : item.browseRecordType, item.businessId, item.h5Url, getNonEmpty(item.title), item.subType);
        });
    }
}

