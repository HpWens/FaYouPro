package com.fy.fayou.search.adapter;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.meis.base.mei.adapter.BaseMultiAdapter;
import com.vondear.rxtool.RxImageTool;

import java.util.List;

import cn.jzvd.JzvdStd;

public class ResultAdapter extends BaseMultiAdapter<SearchResultEntity> {

    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_ITEM_TEMPLATE = 2;
    public static final int TYPE_ITEM_NEWS = 3;
    public static final int TYPE_ITEM_VIDEO = 4;

    public static final int TYPE_ITEM_BOARD = 5;
    public static final int TYPE_ITEM_POST = 6;

    public static final int TYPE_ITEM_PF_VIDEO = 7;


    private String keyword;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ResultAdapter(List<SearchResultEntity> data, String keyword) {
        super(data);
        this.keyword = keyword;
        addItemType(TYPE_HEADER, R.layout.item_result_header);
        addItemType(TYPE_ITEM, R.layout.item_result_content);
        addItemType(TYPE_ITEM_TEMPLATE, R.layout.item_contract_template);
        addItemType(TYPE_ITEM_NEWS, R.layout.item_home_article);
        addItemType(TYPE_ITEM_VIDEO, R.layout.item_search_video_recy);
        addItemType(TYPE_ITEM_BOARD, R.layout.item_search_board);
        addItemType(TYPE_ITEM_POST, R.layout.forum_item_follow);
        addItemType(TYPE_ITEM_PF_VIDEO, R.layout.item_home_video);
    }

    private int getCollectType(int columnType) {
        switch (columnType) {
            case 1:
                return ARoute.JUDICIAL_TYPE;
            case 0:
                return ARoute.LEGAL_TYPE;
            case 3:
                return ARoute.JUDGE_TYPE;
            case 2:
                return ARoute.GUIDE_TYPE;
            case 4:
                return ARoute.TEMPLATE_TYPE;
            default:
                break;
        }
        return ARoute.LEGAL_TYPE;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchResultEntity item) {
        super.convert(helper, item);
        switch (item.getItemType()) {
            case TYPE_HEADER:
                helper.setGone(R.id.view_top, item.headerIndex != 0)
                        .setText(R.id.tv_name, item.name)
                        .setGone(R.id.view_bottom, item.columnType == 4
                                || item.columnType == 6
                                || item.columnType == 5
                                || item.columnType == ARoute.BOARD_TYPE
                                || item.columnType == ARoute.POST_TYPE);

                ImageView ivHeaderView = helper.getView(R.id.iv_header);
                if (item.columnType == ARoute.BOARD_TYPE) {
                    ivHeaderView.setImageResource(R.mipmap.search_header_board_ic);
                } else if (item.columnType == ARoute.POST_TYPE) {
                    ivHeaderView.setImageResource(R.mipmap.search_header_post_ic);
                } else {
                    Glide.with(mContext)
                            .load(getNonEmpty(item.logo))
                            .into((ivHeaderView));
                }

                helper.getView(R.id.tv_more).setOnClickListener(v -> {
//                    if (item.columnType == 5) {
//                        ARoute.jumpLearnClearTask(ARoute.LEARN_NEWS_TAB);
//                    } else if (item.columnType == 6) {
//                        ARoute.jumpLearnClearTask(ARoute.LEARN_HOT_VIDEO_TAB);
//                    } else if (item.columnType == 7) {
//                        ARoute.jumpLearnClearTask(ARoute.LEARN_SMALL_VIDEO_TAB);
//                    } else if (item.columnType == ARoute.BOARD_TYPE) {
//                        ARoute.jumpMoreBoard(keyword);
//                    } else if (item.columnType == ARoute.POST_TYPE) {
//                        ARoute.jumpMorePost(keyword);
//                    } else {
//                        ARoute.jumpModule(getCollectType(item.columnType));
//                    }


                    if (item.columnType == ARoute.POST_TYPE) {
                        ARoute.jumpMorePost(keyword);
                    } else if (item.columnType == ARoute.BOARD_TYPE) {
                        ARoute.jumpMoreBoard(keyword);
                    } else {
                        ARoute.jumpMoreSearchResult(getModuleType(item.columnType), keyword);
                    }

                });

                break;
            case TYPE_ITEM:
                String title = getNonEmpty(TextUtils.isEmpty(item.title) ? item.name : item.title);
                helper.setText(R.id.tv_title, getForeSpan(keyword, title))
                        .setText(R.id.tv_content, getForeSpan(keyword, getNonEmpty(item.content)));

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
            case TYPE_ITEM_NEWS:
                helper.setText(R.id.tv_name, getNonEmpty(item.content))
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

                helper.itemView.setOnClickListener(v -> {
                    ARoute.jumpDetail(item.newsInfoId, item.articleType);
                });

                break;
            case TYPE_ITEM_VIDEO:
                RecyclerView recyclerView = helper.getView(R.id.recycler_video);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                ResultVideoAdapter adapter = new ResultVideoAdapter();
                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        int lastPos = adapter.getData().size() - 1;
                        int pos = parent.getChildAdapterPosition(view);
                        if (lastPos == pos) {
                            outRect.right = RxImageTool.dp2px(15);
                        }
                    }
                });
                if (item.videoList != null && !item.videoList.isEmpty()) {
                    adapter.setNewData(item.videoList);
                }
                break;
            case TYPE_ITEM_BOARD:

                ImageView ivLogo = helper.getView(R.id.iv_thumb);
                Glide.with(mContext)
                        .load(getNonEmpty(item.logo))
                        .apply(GlideOption.getRadiusOption(50, 50, 2))
                        .into(ivLogo);

                helper.setText(R.id.tv_name, getForeSpan(keyword, getNonEmpty(item.name)))
                        .setText(R.id.tv_desc, getForeSpan(keyword, getNonEmpty(item.description)))
                        .setVisible(R.id.view_bottom, !item.isLastChild);

                helper.itemView.setOnClickListener(v -> {
                    ARoute.jumpPlateList(item.id);
                });

                break;
            case TYPE_ITEM_POST:
                helper.setText(R.id.tv_title, getNonEmpty(item.title))
                        .setText(R.id.tv_name, getNonEmpty(item.author))
                        .setText(R.id.tv_content, getNonEmpty(item.description))
                        .setText(R.id.tv_plate, getNonEmpty(item.boardName))
                        .setVisible(R.id.tv_plate, !TextUtils.isEmpty(item.boardName))
                        .setGone(R.id.iv_thumb, !TextUtils.isEmpty(item.cover))
                        .setText(R.id.tv_scan, item.clicks + "人看过")
                        .setText(R.id.tv_comment_num, item.comments + "评论")
                        .setText(R.id.tv_praise_num, item.gives + "点赞");


                ImageView ivHeader = helper.getView(R.id.iv_header);
                Glide.with(mContext)
                        .load(getNonEmpty(item.authorAvatar))
                        .apply(GlideOption.getItemCircleOption(20, 20))
                        .into(ivHeader);

                ImageView ivPostThumb = helper.getView(R.id.iv_thumb);
                Glide.with(mContext)
                        .load(getNonEmpty(item.cover))
                        .apply(GlideOption.getRadiusOption(112, 74, 2))
                        .into(ivPostThumb);

                helper.itemView.setOnClickListener(v -> {
                    ARoute.jumpForumDetail(item.id);
                });
                break;

            case TYPE_ITEM_PF_VIDEO:
                helper.itemView.setTag("video");
                helper.setText(R.id.tv_name, getNonEmpty(item.title))
                        .setText(R.id.tv_origin, getNonEmpty(item.source))
                        .setText(R.id.tv_time, ParseUtils.getTime(item.createTime))
                        .setGone(R.id.iv_praise, false);

                JzvdStd jzvdStd = helper.getView(R.id.video_player);
                jzvdStd.titleTextView.setVisibility(View.INVISIBLE);

                jzvdStd.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                jzvdStd.setUp(getNonEmpty(item.videoUrl), getNonEmpty(item.title));
                Glide.with(mContext)
                        .load(getNonEmpty(item.cover))
                        .apply(GlideOption.getFullScreenWOption(mContext))
                        .into(jzvdStd.thumbImageView);

                // 跳转到视频详情页
                helper.itemView.setOnClickListener(v -> {
                    ARoute.jumpDetail(item.newsInfoId, item.articleType);
                });
                break;
        }
    }

    /**
     * * 模块标识      * 1 法律      * 2 司法解释      * 3 裁判文书      * 4 指导性案例      * 5 合同模板      * 6 普法天地  * 7论坛帖子
     *
     * @param itemType
     * @return
     */
    private int getModuleType(int itemType) {
        int type = 1;
        switch (itemType) {
            case 0:
                type = 1;
                break;
            case 1:
                type = 2;
                break;
            case 2:
                type = 4;
                break;
            case 3:
                type = 3;
                break;
            case 4:
                type = 5;
                break;
            case 5:
                type = 6;
                break;

        }
        return type;
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
