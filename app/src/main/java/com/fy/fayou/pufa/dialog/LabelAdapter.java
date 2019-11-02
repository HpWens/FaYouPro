package com.fy.fayou.pufa.dialog;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.pufa.bean.LabelEntity;

import java.util.ArrayList;

public class LabelAdapter extends BaseQuickAdapter<LabelEntity, BaseViewHolder> {

    public LabelAdapter() {
        super(R.layout.item_dialog_article_label, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LabelEntity item) {

    }
}
