package com.fy.fayou.forum.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.forum.bean.ForumEntity;
import com.fy.fayou.utils.GlideOption;
import com.meis.base.mei.adapter.MeiBaseAdapter;

public class FollowAdapter extends MeiBaseAdapter<ForumEntity> {

    public FollowAdapter() {
        super(R.layout.forum_item_follow);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ForumEntity item) {

        helper.setText(R.id.tv_title, getNonEmpty(item.title))
                .setText(R.id.tv_name, getNonEmpty(item.author))
                .setText(R.id.tv_content, getNonEmpty(item.description))
                .setText(R.id.tv_plate, getNonEmpty(item.boardName))
                .setVisible(R.id.tv_plate, !TextUtils.isEmpty(item.boardName))
                .setText(R.id.tv_scan, item.clicks + "人看过")
                .setText(R.id.tv_comment_num, item.comments + "评论")
                .setText(R.id.tv_praise_num, item.gives + "点赞");


        ImageView ivHeader = helper.getView(R.id.iv_header);
        Glide.with(mContext)
                .load(getNonEmpty(item.authorAvatar))
                .apply(GlideOption.getItemCircleOption(20, 20))
                .into(ivHeader);

        ImageView ivThumb = helper.getView(R.id.iv_thumb);
        Glide.with(mContext)
                .load(getNonEmpty(item.cover))
                .apply(GlideOption.getRadiusOption(112, 74, 2))
                .into(ivThumb);

    }
}
