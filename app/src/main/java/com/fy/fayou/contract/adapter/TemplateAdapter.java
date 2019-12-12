package com.fy.fayou.contract.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.contract.bean.TemplateEntity;
import com.fy.fayou.utils.KtTimeUtils;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class TemplateAdapter extends MeiBaseAdapter<TemplateEntity> {

    public static final int MADE_TEMPLATE = 2;

    private int collectType;

    public TemplateAdapter(int collectType) {
        super(R.layout.item_contract_template, new ArrayList<>());
        this.collectType = collectType;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TemplateEntity item) {
        helper.setText(R.id.tv_num, item.type == MADE_TEMPLATE ? (item.termsNumber + " 条") : "")
                .setText(R.id.tv_type, "")
                .setText(R.id.tv_name, getNonEmpty(item.title))
                .setText(R.id.tv_type, getNonEmpty(item.title))
                .setText(R.id.tv_time, "发布时间：" + KtTimeUtils.INSTANCE.getYMDTime(item.expiryDate))
                .setText(R.id.tv_count, "下载次数：" + item.download + "次");

        helper.itemView.setOnClickListener(v -> {
            if (item.type == MADE_TEMPLATE) {
                ARoute.jumpFilter(item.id, item.title, item.toUrl);
            } else {
                if (item.file != null && !item.file.startsWith("http://")) {
                    item.file = "http://" + item.file;
                }
                ARoute.jumpH5(getNonEmpty(item.toUrl), true, item.id, collectType, item.file);
            }
        });
    }
}
