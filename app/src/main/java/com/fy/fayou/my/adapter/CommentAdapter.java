package com.fy.fayou.my.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.my.bean.CommentEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class CommentAdapter extends MeiBaseAdapter<CommentEntity> {

    public CommentAdapter() {
        super(R.layout.item_my_comment, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CommentEntity item) {

    }
}
