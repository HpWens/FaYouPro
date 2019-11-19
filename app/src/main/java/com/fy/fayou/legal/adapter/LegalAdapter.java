package com.fy.fayou.legal.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.legal.bean.LegalEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class LegalAdapter extends MeiBaseAdapter<LegalEntity> {

    public LegalAdapter() {
        super(R.layout.item_legal, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LegalEntity item) {
        helper.setText(R.id.tv_name, getNonEmpty(item.name))
                .setText(R.id.tv_source, helper.itemView.getResources().getString(R.string.module_source, getNonEmpty(item.source)))
                .setText(R.id.tv_time, helper.itemView.getResources().getString(R.string.module_publish_time, getNonEmpty(item.createTime)));
    }
}
