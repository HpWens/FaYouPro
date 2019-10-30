package com.fy.fayou.my.adapter;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.my.bean.PublishEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class PublishAdapter extends MeiBaseAdapter<PublishEntity> {

    public PublishAdapter() {
        super(R.layout.item_my_publish, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PublishEntity item) {
        ImageView ivSee = helper.getView(R.id.iv_see);
        ivSee.setSelected(false);
    }
}
