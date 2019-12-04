package com.fy.fayou.forum.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.forum.bean.PlateEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuContentAdapter extends MeiBaseAdapter<PlateEntity> {

    private String mKey;

    private List<PlateEntity> selectedArray = new ArrayList<>();

    private OnItemClickListener mListener;

    public MenuContentAdapter(String key, List<PlateEntity> selectedArray) {
        super(R.layout.item_plate_select_content, new ArrayList<>());
        this.mKey = key;
        this.selectedArray = selectedArray;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PlateEntity item) {
        boolean isSelected = isSelected(item.id);
        helper.setText(R.id.tv_name, item.name)
                .setTextColor(R.id.tv_name, helper.itemView.getResources().getColor(isSelected ? R.color.color_ed4040 : R.color.color_333333))
                .setGone(R.id.iv_hook, isSelected);

        helper.itemView.setOnClickListener(v -> {
            // 单选
            selectedArray.clear();
            if (isSelected) {
                notifyItemChanged(helper.getAdapterPosition());
            } else {
                selectedArray.add(item);
                notifyDataSetChanged();
            }
            if (mListener != null) mListener.onClick(v, helper.getAdapterPosition(), selectedArray);
        });
    }

    public boolean isSelected(String id) {
        for (PlateEntity entity : selectedArray) {
            if (entity.id.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(View view, int pos, List<PlateEntity> selectedArray);
    }
}
