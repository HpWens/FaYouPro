package com.fy.fayou.my.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.my.bean.FanEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class FanAdapter extends MeiBaseAdapter<FanEntity> {

    public FanAdapter() {
        super(R.layout.item_my_fan, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FanEntity item) {

    }
}