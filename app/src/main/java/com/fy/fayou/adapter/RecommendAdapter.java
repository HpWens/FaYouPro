package com.fy.fayou.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.bean.RecommendEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.List;

public class RecommendAdapter extends MeiBaseAdapter<RecommendEntity> {

    public RecommendAdapter(@Nullable List<RecommendEntity> data) {
        super(R.layout.item_recommend, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RecommendEntity item) {

    }
}
