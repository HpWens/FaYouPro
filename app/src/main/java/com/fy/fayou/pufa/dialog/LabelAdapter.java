package com.fy.fayou.pufa.dialog;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.bean.TagEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class LabelAdapter extends MeiBaseAdapter<TagEntity> {

    private List<TagEntity> selectedTags = new ArrayList<>();

    private int maxSelectNum = 5;

    private boolean isSingleSelect;

    private TagEntity mCurrentSelectedTag;

    public LabelAdapter() {
        super(R.layout.item_dialog_article_label, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TagEntity item) {
        TextView tvTag = helper.getView(R.id.tv_label);
        tvTag.setText(getNonEmpty(TextUtils.isEmpty(item.tagName) ? item.name : item.tagName));
        tvTag.setSelected(isSelected(item.id));
        tvTag.setOnClickListener(v -> {
            if (isSelected(item.id)) {
                if (mCurrentSelectedTag != null) selectedTags.remove(mCurrentSelectedTag);
            } else {
                if (isSingleSelect) {
                    if (!selectedTags.isEmpty()) {
                        selectedTags.clear();
                        notifyDataSetChanged();
                    }
                    selectedTags.add(item);
                } else {
                    if (selectedTags.size() >= maxSelectNum) {
                        Toast.makeText(mContext, "标签最多只能选择" + maxSelectNum + "个", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    selectedTags.add(item);
                }
            }
            notifyItemChanged(helper.getAdapterPosition());
        });
    }

    private boolean isSelected(String id) {
        for (TagEntity tag : selectedTags) {
            if (tag.id.equals(id)) {
                mCurrentSelectedTag = tag;
                return true;
            }
        }
        return false;
    }

    public void setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
        isSingleSelect = maxSelectNum == 1;
    }

    public List<TagEntity> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(List<TagEntity> selectedTags) {
        this.selectedTags = selectedTags;
    }
}
