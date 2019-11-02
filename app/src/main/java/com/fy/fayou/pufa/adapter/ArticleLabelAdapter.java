package com.fy.fayou.pufa.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.pufa.bean.LabelEntity;

import java.util.ArrayList;

public class ArticleLabelAdapter extends BaseQuickAdapter<LabelEntity, BaseViewHolder> {

    OnItemClickListener mListener;

    public ArticleLabelAdapter(OnItemClickListener listener) {
        super(R.layout.item_article_label, new ArrayList<>());
        mListener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LabelEntity item) {
        boolean isLast = (getData().size() - 1) == helper.getAdapterPosition();

        helper.setVisible(R.id.tv_label, !isLast)
                .setVisible(R.id.iv_delete, !isLast)
                .setVisible(R.id.tv_add, isLast)
                .setVisible(R.id.iv_add, isLast);

        helper.getView(R.id.tv_add).setOnClickListener(v -> {
            mListener.onClick(v);
        });

    }

    public interface OnItemClickListener {
        void onClick(View view);
    }
}
