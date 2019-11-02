package com.fy.fayou.home.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.home.bean.WantedEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class WantedAdapter extends MeiBaseAdapter<WantedEntity> {

    public WantedAdapter() {
        super(R.layout.item_wanted, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WantedEntity item) {

    }
}
