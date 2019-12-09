package com.fy.fayou.forum.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.forum.bean.ForumEntity;
import com.fy.fayou.utils.GlideOption;
import com.meis.base.mei.adapter.MeiBaseAdapter;

public class MorePostAdapter extends MeiBaseAdapter<ForumEntity> {

    private String keyword;

    public MorePostAdapter(String keyword) {
        super(R.layout.forum_item_follow);
        this.keyword = keyword;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ForumEntity item) {
        helper.setText(R.id.tv_title, getForeSpan(keyword, item.title))
                .setText(R.id.tv_name, getNonEmpty(item.author))
                .setText(R.id.tv_content, getForeSpan(keyword, item.description))
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
