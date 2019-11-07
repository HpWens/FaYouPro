package com.fy.fayou.contract.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.contract.bean.TemplateEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class TemplateAdapter extends MeiBaseAdapter<TemplateEntity> {

    public TemplateAdapter() {
        super(R.layout.item_contract_template, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TemplateEntity item) {

    }
}
