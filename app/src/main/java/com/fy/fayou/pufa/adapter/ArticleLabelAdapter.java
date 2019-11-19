package com.fy.fayou.pufa.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.bean.TagEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ArticleLabelAdapter extends MeiBaseAdapter<TagEntity> {

    OnItemClickListener mListener;

    public ArticleLabelAdapter(OnItemClickListener listener) {
        super(R.layout.item_article_label, new ArrayList<>());
        mListener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TagEntity item) {
        boolean isLast = (getData().size() - 1) == helper.getAdapterPosition();

        helper.setVisible(R.id.tv_label, !isLast)
                .setText(R.id.tv_label, getNonEmpty(item.tagName))
                .setVisible(R.id.iv_delete, !isLast)
                .setVisible(R.id.tv_add, isLast)
                .setVisible(R.id.iv_add, isLast);

        helper.getView(R.id.tv_add).setOnClickListener(v -> {
            mListener.onClick(v);
        });

        helper.getView(R.id.iv_delete).setOnClickListener(v -> {
            getData().remove(helper.getAdapterPosition());
            notifyItemRemoved(helper.getAdapterPosition());
        });

    }

    public interface OnItemClickListener {
        void onClick(View view);
    }

    /**
     * 获取选中的标签数据
     *
     * @return
     */
    public List<TagEntity> getTags() {
        List<TagEntity> tags = new ArrayList<>();
        for (TagEntity tag : getData()) {
            if (!TextUtils.isEmpty(tag.id)) {
                tags.add(tag);
            }
        }
        return tags;
    }
}
