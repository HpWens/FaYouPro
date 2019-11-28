package com.fy.fayou.search.adapter;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.search.bean.SearchResultEntity;
import com.fy.fayou.utils.GlideOption;
import com.meis.base.mei.adapter.MeiBaseAdapter;

public class ResultVideoAdapter extends MeiBaseAdapter<SearchResultEntity> {

    public ResultVideoAdapter() {
        super(R.layout.item_search_result_video);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchResultEntity item) {

        helper.setText(R.id.tv_name, item.title);

        ImageView ivCover = helper.getView(R.id.iv_cover);

        Glide.with(mContext)
                .load(item.cover)
                .apply(GlideOption.getRadiusOption(160, 90, 2))
                .into(ivCover);

        helper.itemView.setOnClickListener(v -> {
            ARoute.jumpDetail(item.newsInfoId, item.articleType);
        });
    }
}
