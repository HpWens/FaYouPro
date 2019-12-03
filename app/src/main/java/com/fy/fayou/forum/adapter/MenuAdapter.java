package com.fy.fayou.forum.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.search.bean.ColumnEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class MenuAdapter extends MeiBaseAdapter<ColumnEntity> {

    public MenuAdapter() {
        super(R.layout.item_plate_select_menu, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ColumnEntity item) {

    }
}
