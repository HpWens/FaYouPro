package com.fy.fayou.forum.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.forum.bean.ForumEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

public class FollowAdapter extends MeiBaseAdapter<ForumEntity> {

    public FollowAdapter() {
        super(R.layout.forum_item_follow);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ForumEntity item) {

    }
}
