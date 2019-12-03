package com.fy.fayou.forum.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.search.bean.ColumnEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class MenuContentAdapter extends MeiBaseAdapter<ColumnEntity> {

    private String mKey;

    public MenuContentAdapter(String key) {
        super(R.layout.item_plate_select_content, new ArrayList<>());
        this.mKey = key;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ColumnEntity item) {

    }
}
