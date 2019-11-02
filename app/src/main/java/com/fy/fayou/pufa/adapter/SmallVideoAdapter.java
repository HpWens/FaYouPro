package com.fy.fayou.pufa.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.pufa.bean.SmallVideoEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class SmallVideoAdapter extends MeiBaseAdapter<SmallVideoEntity> {

    public SmallVideoAdapter() {
        super(R.layout.item_home_small_video, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SmallVideoEntity item) {

    }
}
