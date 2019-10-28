package com.fy.fayou.search.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.search.bean.SearchEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class AssociateAdapter extends MeiBaseAdapter<SearchEntity> {

    public AssociateAdapter() {
        super(R.layout.item_search_associate, new ArrayList<>());
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchEntity item) {

    }
}
