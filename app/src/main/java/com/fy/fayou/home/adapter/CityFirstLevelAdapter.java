package com.fy.fayou.home.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.bean.City;

import java.util.ArrayList;
import java.util.List;

public class CityFirstLevelAdapter extends BaseQuickAdapter<City, BaseViewHolder> {

    private int mLastCheckedPosition = -1;
    private SparseBooleanArray mBooleanArray;
    private OnItemClickListener mListener;

    public CityFirstLevelAdapter() {
        super(R.layout.item_city_first_level, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, City item) {
        boolean isSelected = mBooleanArray.get(helper.getAdapterPosition());
        helper.setVisible(R.id.view_line, isSelected).setText(R.id.tv_name, item.name);
        int selectedBgColor = helper.itemView.getResources().getColor(!isSelected ? R.color.color_f5f5f5 : R.color.color_ffffff);
        helper.itemView.setBackgroundColor(selectedBgColor);

        helper.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick(helper.getAdapterPosition(), item.children);
            }
        });
    }

    @Override
    public void setNewData(@Nullable List<City> data) {
        mBooleanArray = new SparseBooleanArray(data.size());
        super.setNewData(data);
    }

    public void setItemChecked(int position) {
        mBooleanArray.put(position, true);

        if (mLastCheckedPosition > -1) {
            mBooleanArray.put(mLastCheckedPosition, false);
            notifyItemChanged(mLastCheckedPosition);
        }
        notifyDataSetChanged();

        mLastCheckedPosition = position;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(int pos, List<City> data);
    }
}
