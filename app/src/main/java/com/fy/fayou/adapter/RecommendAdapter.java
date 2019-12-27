package com.fy.fayou.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.bean.RecommendEntity;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.ParseUtils;
import com.fy.fayou.view.SampleCoverVideo;
import com.meis.base.mei.adapter.BaseMultiAdapter;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.ArrayList;

public class RecommendAdapter extends BaseMultiAdapter<RecommendEntity> {

    private OnItemListener mListener;

    // 视频相关
    GSYVideoOptionBuilder gsyVideoOptionBuilder;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     */
    public RecommendAdapter() {
        super(new ArrayList<>());
        addItemType(RecommendEntity.TYPE_TOP, R.layout.item_home_top);
        addItemType(RecommendEntity.TYPE_ARTICLE, R.layout.item_home_article);
        addItemType(RecommendEntity.TYPE_VIDEO, R.layout.item_home_video);

        gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RecommendEntity item) {
        switch (helper.getItemViewType()) {
            case RecommendEntity.TYPE_TOP:
                helper.itemView.setTag("top");
                helper.setText(R.id.tv_name, getNonEmpty(item.fullTitle))
                        .setText(R.id.tv_origin, getNonEmpty(item.source))
                        .setText(R.id.tv_time, ParseUtils.getTime(item.createTime))
                        .setGone(R.id.tv_origin, !TextUtils.isEmpty(item.source))
                        .setText(R.id.tv_scan_num, item.clicks + " 阅")
                        .setGone(R.id.tv_scan_num, item.clicks >= 0);

                ImageView ivCover = helper.getView(R.id.fl_video);
                if (TextUtils.isEmpty(item.id)) {
                    if (item.fixedMode == 1) {
                        ivCover.setImageResource(R.mipmap.ic_home_top_0);
                    } else {
                        ivCover.setImageResource(R.mipmap.ic_home_top_1);
                    }
                    helper.setVisible(R.id.fl_video, true);
                } else {
                    Glide.with(mContext)
                            .load(getNonEmpty(item.cover))
                            .apply(GlideOption.getItemOption(112, 74))
                            .into(ivCover);
                    helper.setGone(R.id.fl_video, !TextUtils.isEmpty(item.cover));
                }

                // 跳转到详情页
                helper.itemView.setOnClickListener(v -> {
                    if (mListener != null) {
                        if (TextUtils.isEmpty(item.id)) {
                            if (item.fixedMode == 1) {
                                ARoute.jumpWanted();
                            } else {
                                ARoute.jumpH5("http://fayou-h5.zhdfxm.com/myCountry");
                            }
                        } else {
                            mListener.onClick(v, helper.getAdapterPosition(), item);
                        }
                    }
                });

                break;
            case RecommendEntity.TYPE_ARTICLE:
                helper.itemView.setTag("article");

                helper.setText(R.id.tv_name, getNonEmpty(item.fullTitle))
                        .setText(R.id.tv_origin, getNonEmpty(item.source))
                        .setText(R.id.tv_time, ParseUtils.getTime(item.createTime))
                        .setGone(R.id.tv_origin, !TextUtils.isEmpty(item.source))
                        .setText(R.id.tv_scan_num, item.clicks + " 阅")
                        .setGone(R.id.tv_scan_num, item.clicks >= 0);

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

                // 跳转到文章详情页
                helper.itemView.setOnClickListener(v -> {
                    if (mListener != null) {
                        mListener.onClick(v, helper.getAdapterPosition(), item);
                    }
                });

                break;
            case RecommendEntity.TYPE_VIDEO:
                helper.itemView.setTag("video");
                helper.setText(R.id.tv_name, getNonEmpty(item.fullTitle))
                        .setText(R.id.tv_origin, getNonEmpty(item.source))
                        .setText(R.id.tv_time, ParseUtils.getTime(item.createTime))
                        .setText(R.id.tv_scan_num, item.clicks + " 阅")
                        .setGone(R.id.tv_scan_num, item.clicks >= 0);

                helper.getView(R.id.iv_praise).setSelected(item.give);

                // 跳转到视频详情页
                helper.itemView.setOnClickListener(v -> {
                    if (mListener != null) {
                        mListener.onClick(v, helper.getAdapterPosition(), item);
                    }
                });

                // 处理点赞
                helper.getView(R.id.iv_praise).setOnClickListener(v -> {
                    if (mListener != null) {
                        mListener.onPraise(v, helper.getAdapterPosition(), item);
                    }
                });

                SampleCoverVideo gsyVideoPlayer = helper.getView(R.id.video_item_player);
                gsyVideoPlayer.loadCoverImage(getNonEmpty(item.cover), R.mipmap.base_net_error);
                gsyVideoOptionBuilder
                        .setIsTouchWiget(false)
                        .setUrl(getNonEmpty(item.videoUrl))
                        .setVideoTitle(getNonEmpty(item.fullTitle))
                        .setCacheWithPlay(false)
                        .setRotateViewAuto(true)
                        .setLockLand(true)
                        .setPlayTag("video")
                        .setShowFullAnimation(true)
                        .setAutoFullWithSize(true)
                        .setNeedLockFull(true)
                        .setPlayPosition(helper.getAdapterPosition())
                        .setVideoAllCallBack(new GSYSampleCallBack() {
                            @Override
                            public void onPrepared(String url, Object... objects) {
                                super.onPrepared(url, objects);
                                if (!gsyVideoPlayer.isIfCurrentIsFullscreen()) {
                                    //静音
                                    //GSYVideoManager.instance().setNeedMute(true);
                                }

                            }

                            @Override
                            public void onQuitFullscreen(String url, Object... objects) {
                                super.onQuitFullscreen(url, objects);
                                //全屏不静音
                                //GSYVideoManager.instance().setNeedMute(true);
                            }

                            @Override
                            public void onEnterFullscreen(String url, Object... objects) {
                                super.onEnterFullscreen(url, objects);
                                //GSYVideoManager.instance().setNeedMute(false);
                                gsyVideoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                            }
                        }).build(gsyVideoPlayer);


                //增加title
                gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);

                //设置返回键
                gsyVideoPlayer.getBackButton().setVisibility(View.GONE);

                //设置全屏按键功能
                gsyVideoPlayer.getFullscreenButton().setOnClickListener(v -> resolveFullBtn(gsyVideoPlayer));

                break;
            default:
                break;
        }
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(mContext, true, true);
    }

    public void setOnItemListener(OnItemListener listener) {
        mListener = listener;
    }

    public interface OnItemListener {
        void onClick(View v, int position, RecommendEntity item);

        void onPraise(View v, int position, RecommendEntity entity);
    }
}
