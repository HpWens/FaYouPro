package com.fy.fayou.detail.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.detail.bean.LawBean;
import com.meis.base.mei.adapter.BaseMultiAdapter;

import java.util.List;

public class RelatedAdapter extends BaseMultiAdapter<LawBean> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public RelatedAdapter(List<LawBean> data) {
        super(data);
        addItemType(0, R.layout.item_related_level1);
        addItemType(1, R.layout.item_related_level2);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, LawBean item) {
        super.convert(helper, item);
        if (item.getItemType() == 0) {
            helper.setText(R.id.tv_name, getNonEmpty(item.name));
        } else {
            helper.setText(R.id.tv_name, getNonEmpty(item.name));
            helper.itemView.setOnClickListener(v -> {
                ARoute.jumpH5(getNonEmpty(item.url), true, item.id, item.collectType);
            });
        }
    }
}
