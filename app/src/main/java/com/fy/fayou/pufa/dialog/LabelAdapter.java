package com.fy.fayou.pufa.dialog;

import android.support.annotation.NonNull;
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

    public LabelAdapter() {
        super(R.layout.item_dialog_article_label, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TagEntity item) {
        TextView tvTag = helper.getView(R.id.tv_label);
        tvTag.setText(getNonEmpty(item.tagName));
        tvTag.setSelected(isSelected(item.id));
        tvTag.setOnClickListener(v -> {
            if (isSelected(item.id)) {
                selectedTags.remove(item);
            } else {
                if (selectedTags.size() >= maxSelectNum) {
                    Toast.makeText(mContext, "标签最多只能选择5个", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedTags.add(item);
            }
            notifyItemChanged(helper.getAdapterPosition());
        });
    }

    private boolean isSelected(String id) {
        for (TagEntity tag : selectedTags) {
            if (tag.id.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public List<TagEntity> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(List<TagEntity> selectedTags) {
        this.selectedTags = selectedTags;
    }
}
