package com.fy.fayou.search.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;

import java.util.ArrayList;

public class FlowAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    OnItemClickListener mListener;

    public FlowAdapter(OnItemClickListener listener) {
        super(R.layout.item_flow_search_history, new ArrayList<>());
        this.mListener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name, item);

        helper.getView(R.id.tv_name).setOnClickListener(v -> mListener.onClick(v, item));
    }

    public interface OnItemClickListener {
        void onClick(View v, String key);
    }
}
