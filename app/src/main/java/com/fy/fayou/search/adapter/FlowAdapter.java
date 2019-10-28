package com.fy.fayou.search.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;

import java.util.ArrayList;

public class FlowAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public FlowAdapter() {
        super(R.layout.item_flow_search_history, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name, item);
    }
}
