package com.fy.fayou.legal.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.legal.bean.LegalEntity;
import com.fy.fayou.utils.KtTimeUtils;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class LegalAdapter extends MeiBaseAdapter<LegalEntity> {

    // 标识 取不同列表字段
    private int type;

    private int collectType;

    public LegalAdapter(int type, int collectType) {
        super(R.layout.item_legal, new ArrayList<>());
        this.type = type;
        this.collectType = collectType;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LegalEntity item) {

        if (collectType == ARoute.GUIDE_TYPE) {
            helper.setText(R.id.tv_name, item.title)
                    .setText(R.id.tv_source, helper.itemView.getResources().getString(R.string.module_source, getNonEmpty(item.source)))
                    .setText(R.id.tv_time, helper.itemView.getResources().getString(R.string.module_publish_time, item.publicDate));

            helper.itemView.setOnClickListener(v -> {
                ARoute.jumpH5(getNonEmpty(item.url), true, item.id, collectType);
            });
        } else {
            helper.setText(R.id.tv_name, getNonEmpty(type == 3 ? item.name : item.title))
                    .setText(R.id.tv_source, helper.itemView.getResources().getString(R.string.module_source, getNonEmpty(item.source)))
                    .setText(R.id.tv_time, helper.itemView.getResources().getString(R.string.module_publish_time, KtTimeUtils.INSTANCE.getYMDTime(type == 3 ? item.publishTime : item.pubTime)));

            helper.itemView.setOnClickListener(v -> {
                ARoute.jumpH5(getNonEmpty(item.toUrl), true, item.id, collectType);
            });
        }
    }
}
