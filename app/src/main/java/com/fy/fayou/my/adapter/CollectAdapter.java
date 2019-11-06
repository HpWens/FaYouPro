package com.fy.fayou.my.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.my.bean.CollectEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class CollectAdapter extends MeiBaseAdapter<CollectEntity> {

    public CollectAdapter() {
        super(R.layout.item_my_collect, new ArrayList<>());
    }

    @Override
    public void convert(BaseViewHolder holder, CollectEntity item) {

    }
}

