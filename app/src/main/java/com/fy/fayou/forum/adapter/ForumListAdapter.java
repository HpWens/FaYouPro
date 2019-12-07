package com.fy.fayou.forum.adapter;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.forum.bean.ForumEntity;
import com.fy.fayou.utils.GlideOption;
import com.meis.base.mei.adapter.BaseMultiAdapter;

import java.util.ArrayList;

public class ForumListAdapter extends BaseMultiAdapter<ForumEntity> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     */
    public ForumListAdapter() {
        super(new ArrayList<>());
        addItemType(1, R.layout.item_forum_list_top);
        addItemType(0, R.layout.item_forum_list_content);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ForumEntity item) {
        super.convert(helper, item);
        switch (item.getItemType()) {
            case 0:
                ImageView ivThumb = helper.getView(R.id.iv_header);
                Glide.with(mContext)
                        .load(getNonEmpty(item.authorAvatar))
                        .apply(GlideOption.getItemCircleOption(20, 20))
                        .into(ivThumb);

                helper.setText(R.id.tv_name, getNonEmpty(item.author))
                        .setText(R.id.tv_title, getNonEmpty(item.title))
                        .setText(R.id.tv_scan_num, item.clicks + "人看过")
                        .setText(R.id.tv_comment_num, item.comments + "评论")
                        .setText(R.id.tv_praise_num, item.gives + "点赞");

                helper.itemView.setOnClickListener(v -> {
                    ARoute.jumpForumDetail(item.id);
                });
                break;
            case 1:
                helper.setText(R.id.tv_content, getNonEmpty(item.title));
                break;
        }
    }

}
