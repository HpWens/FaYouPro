package com.fy.fayou.detail.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fy.fayou.R;
import com.meis.base.mei.adapter.BaseMultiAdapter;

import java.util.ArrayList;

public class ReviewAdapter extends BaseMultiAdapter<MultiItemEntity> {

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    public ReviewAdapter() {
        super(new ArrayList<>());
        addItemType(TYPE_LEVEL_0, R.layout.item_comment_level0);
        addItemType(TYPE_LEVEL_1, R.layout.item_comment_level1);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiItemEntity item) {

    }
}
