package com.fy.fayou.detail.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.detail.bean.RecommendBean;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.adapter.MultiItemPresenter;

import cn.jzvd.JzvdStd;

public class RecommendPresenter extends MultiItemPresenter<RecommendBean> {

    private OnItemListener mListener;

    private int ARTICLE_TYPE = 1;
    private int VIDEO_TYPE = 2;

    public RecommendPresenter(OnItemListener listener) {
        mListener = listener;
    }

    @Override
    public int getLayoutRes(int itemType) {
        if (itemType == ARTICLE_TYPE) {
            return R.layout.item_home_article;
        }
        return R.layout.item_home_video;
    }

    @Override
    public int getItemType(RecommendBean item) {
        if (TextUtils.isEmpty(item.articleType)) {
            return ARTICLE_TYPE;
        }
        if (item.articleType.equals("ARTICLE")) {
            return ARTICLE_TYPE;
        }
        return VIDEO_TYPE;
    }

    @Override
    public int[] getItemTypes() {
        return new int[]{ARTICLE_TYPE, VIDEO_TYPE};
    }

    @Override
    public void convert(BaseViewHolder helper, RecommendBean item) {
        switch (item.articleType) {
            case "VIDEO":
                helper.itemView.setTag("video");
                helper.setText(R.id.tv_name, getNonEmpty(item.fullTitle))
                        .setText(R.id.tv_origin, getNonEmpty(item.source))
                        .setText(R.id.tv_time, ParseUtils.getTime(item.createTime));

                JzvdStd jzvdStd = helper.getView(R.id.video_player);
                jzvdStd.titleTextView.setVisibility(View.INVISIBLE);

                jzvdStd.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                jzvdStd.setUp(getNonEmpty(item.videoUrl), getNonEmpty(item.fullTitle));
                Glide.with(helper.itemView.getContext())
                        .load(getNonEmpty(item.cover))
                        .apply(GlideOption.getFullScreenWOption(helper.itemView.getContext()))
                        .into(jzvdStd.thumbImageView);

                // 跳转到视频详情页
                helper.itemView.setOnClickListener(v -> {
                    if (mListener != null) {
                        mListener.onClick(v, item);
                    }
                });
                break;
            case "ARTICLE":
                helper.itemView.setTag("article");
                helper.setText(R.id.tv_name, getNonEmpty(item.fullTitle))
                        .setText(R.id.tv_origin, getNonEmpty(item.source))
                        .setText(R.id.tv_time, ParseUtils.getTime(item.createTime))
                        .setGone(R.id.iv_top, item.showIndex);

                ImageView ivThumb = helper.getView(R.id.iv_thumb);
                if (TextUtils.isEmpty(item.cover)) {
                    ivThumb.setVisibility(View.GONE);
                } else {
                    ivThumb.setVisibility(View.VISIBLE);
                    Glide.with(helper.itemView.getContext())
                            .load(item.cover)
                            .apply(GlideOption.getRadiusOption(112, 74, 2))
                            .into(ivThumb);
                }

                // 跳转到文章详情页
                helper.itemView.setOnClickListener(v -> {
                    if (mListener != null) {
                        mListener.onClick(v, item);
                    }
                });
                break;
            default:
                break;
        }
    }

    public void setOnItemListener(OnItemListener listener) {
        mListener = listener;
    }

    public interface OnItemListener {
        void onClick(View v, RecommendBean item);
    }
}
