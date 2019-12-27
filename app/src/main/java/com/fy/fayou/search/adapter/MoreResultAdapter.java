package com.fy.fayou.search.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.search.bean.SearchResultEntity;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.KtTimeUtils;
import com.fy.fayou.utils.ParseUtils;
import com.fy.fayou.view.SampleCoverVideo;
import com.meis.base.mei.adapter.BaseMultiAdapter;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.ArrayList;

public class MoreResultAdapter extends BaseMultiAdapter<SearchResultEntity> {

    public static final int TYPE_ITEM_TEMPLATE = 1;
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_ITEM_ARTICLE = 2;
    public static final int TYPE_ITEM_VIDEO = 3;

    private String mKeyword;

    // 视频相关
    GSYVideoOptionBuilder gsyVideoOptionBuilder;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     */
    public MoreResultAdapter(String keyword) {
        super(new ArrayList<>());
        mKeyword = keyword;
        addItemType(TYPE_ITEM, R.layout.item_result_content);
        addItemType(TYPE_ITEM_TEMPLATE, R.layout.item_contract_template);
        addItemType(TYPE_ITEM_ARTICLE, R.layout.item_home_article);
        addItemType(TYPE_ITEM_VIDEO, R.layout.item_home_video);

        gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchResultEntity item) {
        super.convert(helper, item);
        switch (item.getItemType()) {
            case TYPE_ITEM:
                String title = getNonEmpty(TextUtils.isEmpty(item.title) ? item.name : item.title);
                helper.setText(R.id.tv_title, getForeSpan(mKeyword, title))
                        .setText(R.id.tv_content, getForeSpan(mKeyword, getNonEmpty(item.content)))
                        .setGone(R.id.view_top, false)
                        .setGone(R.id.view_bottom, true);

                helper.itemView.setOnClickListener(v -> {
                    if (!TextUtils.isEmpty(item.toUrl)) {
                        try {
                            String id = item.toUrl.substring(item.toUrl.lastIndexOf("/") + 1);
                            ARoute.jumpH5(item.toUrl, true, id, getCollectType(item.columnType), title);
                        } catch (Exception e) {
                        }
                    }
                });
                break;
            case TYPE_ITEM_TEMPLATE:
                helper.setText(R.id.tv_num, item.termsNumber + " 条")
                        .setText(R.id.tv_type, "")
                        .setText(R.id.tv_name, getNonEmpty(item.title))
                        .setText(R.id.tv_type, getNonEmpty(item.title))
                        .setText(R.id.tv_time, "发布时间：" + KtTimeUtils.INSTANCE.getYMDTime(item.expiryDate))
                        .setText(R.id.tv_count, "下载次数：" + item.download + "次")
                        .setVisible(R.id.view_bottom, !item.isLastChild)
                        .setGone(R.id.tv_num, !item.type.equals("1"));

                helper.itemView.setOnClickListener(v -> {
                    if (!item.type.equals("1")) {
                        ARoute.jumpFilter(item.id, item.title, item.toUrl);
                        return;
                    }
                    ARoute.jumpH5(getNonEmpty(item.toUrl), true, item.id, ARoute.TEMPLATE_TYPE, getNonEmpty(item.title));
                });
                break;
            case TYPE_ITEM_ARTICLE:
                helper.setText(R.id.tv_name, getNonEmpty(item.title))
                        .setText(R.id.tv_origin, getNonEmpty(item.source))
                        .setText(R.id.tv_time, ParseUtils.getTime(item.createTime))
                        .setGone(R.id.tv_origin, !TextUtils.isEmpty(item.source));

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

                helper.itemView.setOnClickListener(v -> {
                    ARoute.jumpDetail(item.newsInfoId, item.articleType);
                });
                break;
            case TYPE_ITEM_VIDEO:
                helper.itemView.setTag("video");
                helper.setText(R.id.tv_name, getNonEmpty(item.title))
                        .setText(R.id.tv_origin, getNonEmpty(item.source))
                        .setText(R.id.tv_time, ParseUtils.getTime(item.createTime))
                        .setGone(R.id.iv_praise, false);

                SampleCoverVideo gsyVideoPlayer = helper.getView(R.id.video_item_player);
                gsyVideoPlayer.loadCoverImage(getNonEmpty(item.cover), R.mipmap.base_net_error);
                gsyVideoOptionBuilder
                        .setIsTouchWiget(false)
                        .setUrl(getNonEmpty(item.videoUrl))
                        .setVideoTitle(getNonEmpty(item.title))
                        .setCacheWithPlay(false)
                        .setRotateViewAuto(true)
                        .setLockLand(true)
                        .setPlayTag("video")
                        .setShowFullAnimation(true)
                        .setNeedLockFull(true)
                        .setAutoFullWithSize(true)
                        .setPlayPosition(helper.getAdapterPosition())
                        .setVideoAllCallBack(new GSYSampleCallBack() {
                            @Override
                            public void onPrepared(String url, Object... objects) {
                                super.onPrepared(url, objects);
                                if (!gsyVideoPlayer.isIfCurrentIsFullscreen()) {
                                    //静音
                                    // GSYVideoManager.instance().setNeedMute(true);
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

                // 跳转到视频详情页
                helper.itemView.setOnClickListener(v -> {
                    ARoute.jumpDetail(item.newsInfoId, item.articleType);
                });

                // 跳转到视频详情页
                helper.itemView.setOnClickListener(v -> {
                    ARoute.jumpDetail(item.newsInfoId, item.articleType);
                });
                break;
        }
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(mContext, true, true);
    }

    /**
     * * 1 法律      * 2 司法解释      * 3 裁判文书      * 4 指导性案例      * 5 合同模板      * 6 普法天地  * 7论坛帖子
     *
     * @param columnType
     * @return
     */
    private int getCollectType(int columnType) {
        switch (columnType) {
            case 1:
                return ARoute.LEGAL_TYPE;
            case 2:
                return ARoute.JUDICIAL_TYPE;
            case 3:
                return ARoute.JUDGE_TYPE;
            case 4:
                return ARoute.GUIDE_TYPE;
            default:
                break;
        }
        return ARoute.LEGAL_TYPE;
    }

    /**
     * @param span
     * @param content
     * @return
     */
    public SpannableString getForeSpan(String span, String content) {
        if (!TextUtils.isEmpty(content)) {
            content = content.replaceAll("\n", "");
        } else {
            content = "";
        }
        SpannableString spannableString = new SpannableString(content);
        if (content.contains(span)) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ED4040"));
            spannableString.setSpan(colorSpan, content.indexOf(span), content.indexOf(span) + span.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return spannableString;
    }
}
