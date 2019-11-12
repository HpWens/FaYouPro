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

    }
}
