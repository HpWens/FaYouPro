package com.fy.fayou.my.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.my.bean.PostEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class PostAdapter extends MeiBaseAdapter<PostEntity> {

    public PostAdapter() {
        super(R.layout.item_my_post, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PostEntity item) {

    }
}
