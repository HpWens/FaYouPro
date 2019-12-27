package com.fy.fayou.detail.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.detail.bean.RecommendBean;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.ParseUtils;
import com.fy.fayou.view.SampleCoverVideo;
import com.meis.base.mei.adapter.MultiItemPresenter;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class RecommendPresenter extends MultiItemPresenter<RecommendBean> {

    private OnItemListener mListener;

    private int ARTICLE_TYPE = 1;
    private int VIDEO_TYPE = 2;

    // 视频相关
    GSYVideoOptionBuilder gsyVideoOptionBuilder;

    public RecommendPresenter(OnItemListener listener) {
        mListener = listener;
        gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
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
                gsyVideoPlayer.getFullscreenButton().setOnClickListener(v -> resolveFullBtn(helper.itemView.getContext(), gsyVideoPlayer));

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
                        .setGone(R.id.iv_top, item.showIndex)
                        .setGone(R.id.tv_origin, !TextUtils.isEmpty(item.source));

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

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(Context context, final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(context, true, true);
    }
}
