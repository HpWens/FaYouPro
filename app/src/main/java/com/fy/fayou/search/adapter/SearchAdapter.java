package com.fy.fayou.search.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.search.bean.SearchEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

public class SearchAdapter extends MeiBaseAdapter<SearchEntity> {

    public SearchAdapter() {
        super(R.layout.item_hot_search);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchEntity item) {
        boolean isHasHeader = getHeaderLayoutCount() == 1 ? true : false;
        helper.setGone(R.id.tv_hot, isHasHeader ? helper.getAdapterPosition() == 1 : helper.getAdapterPosition() == 0);
    }
}
