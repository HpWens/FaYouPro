package com.fy.fayou.my.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.pufa.bean.NewsEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class NewsListAdapter extends MeiBaseAdapter<NewsEntity> {

    public NewsListAdapter() {
        super(R.layout.item_my_news, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewsEntity item) {

    }
}
