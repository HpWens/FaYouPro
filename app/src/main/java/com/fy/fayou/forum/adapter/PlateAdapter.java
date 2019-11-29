package com.fy.fayou.forum.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.forum.bean.PlateEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

public class PlateAdapter extends MeiBaseAdapter<PlateEntity> {

    public PlateAdapter() {
        super(R.layout.item_forum_home_plate);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PlateEntity item) {

    }
}
