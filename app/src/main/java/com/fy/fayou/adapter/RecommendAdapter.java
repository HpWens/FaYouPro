package com.fy.fayou.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.bean.RecommendEntity;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.adapter.BaseMultiAdapter;

import java.util.ArrayList;

import cn.jzvd.JzvdStd;

public class RecommendAdapter extends BaseMultiAdapter<RecommendEntity> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     */
    public RecommendAdapter() {
        super(new ArrayList<>());
        addItemType(RecommendEntity.TYPE_TOP, R.layout.item_home_top);
        addItemType(RecommendEntity.TYPE_ARTICLE, R.layout.item_home_article);
        addItemType(RecommendEntity.TYPE_VIDEO, R.layout.item_home_video);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RecommendEntity item) {
        switch (helper.getItemViewType()) {
            case RecommendEntity.TYPE_TOP:
                helper.itemView.setTag("top");
                helper.setText(R.id.tv_name, getNonEmpty(item.fullTitle))
                        .setText(R.id.tv_origin, getNonEmpty(item.source))
                        .setText(R.id.tv_time, ParseUtils.getTime(item.createTime));

                ImageView ivCover = helper.getView(R.id.fl_video);
                Glide.with(mContext)
                        .load(getNonEmpty(item.cover))
                        .apply(GlideOption.getItemOption(112, 74))
                        .into(ivCover);

                break;
            case RecommendEntity.TYPE_ARTICLE:
                helper.itemView.setTag("article");

                helper.setText(R.id.tv_name, getNonEmpty(item.fullTitle))
                        .setText(R.id.tv_origin, getNonEmpty(item.source))
                        .setText(R.id.tv_time, ParseUtils.getTime(item.createTime));

                ImageView ivThumb = helper.getView(R.id.iv_thumb);
                if (TextUtils.isEmpty(item.cover)) {
                    ivThumb.setVisibility(View.GONE);
                } else {
                    ivThumb.setVisibility(View.VISIBLE);
                    Glide.with(mContext)
                            .load(item.cover)
                            .apply(GlideOption.getRadiusOption(112, 74, 2))
                            .into(ivThumb);
                }
                break;
            case RecommendEntity.TYPE_VIDEO:
                helper.itemView.setTag("video");
                helper.setText(R.id.tv_name, getNonEmpty(item.fullTitle))
                        .setText(R.id.tv_origin, getNonEmpty(item.source))
                        .setText(R.id.tv_time, ParseUtils.getTime(item.createTime));

                JzvdStd jzvdStd = helper.getView(R.id.video_player);
                jzvdStd.titleTextView.setVisibility(View.INVISIBLE);

                jzvdStd.setUp(getNonEmpty(item.videoUrl), getNonEmpty(item.fullTitle));
                Glide.with(mContext)
                        .load(getNonEmpty(item.cover))
                        .apply(GlideOption.getFullScreenWOption(mContext))
                        .into(jzvdStd.thumbImageView);

                break;
            default:
                break;
        }
    }
}
