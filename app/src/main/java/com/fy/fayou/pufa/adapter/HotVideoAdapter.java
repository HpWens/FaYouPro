package com.fy.fayou.pufa.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.pufa.bean.HotVideoEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class HotVideoAdapter extends MeiBaseAdapter<HotVideoEntity> {

    public HotVideoAdapter() {
        super(R.layout.item_home_small_video, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HotVideoEntity item) {

    }
}
