package com.fy.fayou.search.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.search.bean.SearchEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

public class SearchAdapter extends MeiBaseAdapter<SearchEntity> {

    OnItemListener mListener;

    public SearchAdapter(OnItemListener listener) {
        super(R.layout.item_hot_search);
        mListener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchEntity item) {
        boolean isHasHeader = getHeaderLayoutCount() == 1 ? true : false;
        helper.setGone(R.id.tv_hot, isHasHeader ? helper.getAdapterPosition() == 1 : helper.getAdapterPosition() == 0);

        int rank = isHasHeader ? helper.getAdapterPosition() : helper.getAdapterPosition() + 1;
        helper.setText(R.id.tv_name, item.keyword)
                .setText(R.id.tv_num, rank + "")
                .setTextColor(R.id.tv_num, rank > 3 ? Color.parseColor("#d2d2d2") : Color.parseColor("#FF873F"));

        helper.getView(R.id.tv_name).setOnClickListener(v -> {
            mListener.onClick(v, item.keyword);
        });

    }

    public interface OnItemListener {
        void onClick(View v, String keyword);
    }
}
