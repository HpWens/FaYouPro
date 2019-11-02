package com.fy.fayou.search.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.search.bean.SearchResultEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class ResultContentAdapter extends MeiBaseAdapter<SearchResultEntity> {

    public ResultContentAdapter() {
        super(R.layout.item_result_content, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchResultEntity item) {

    }
}
