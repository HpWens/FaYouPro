package com.fy.fayou.search.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.search.bean.ColumnEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuListAdapter extends MeiBaseAdapter<ColumnEntity> {

    private int mLastCheckedPosition = -1;
    private SparseBooleanArray mBooleanArray;
    private OnItemClickListener mListener;

    public MenuListAdapter(OnItemClickListener listener) {
        super(R.layout.item_search_result_list, new ArrayList<>());
        this.mListener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ColumnEntity item) {
        helper.setText(R.id.tv_name, item.name);

        boolean isSelected = mBooleanArray.get(helper.getAdapterPosition());
        helper.setVisible(R.id.view_line, isSelected);
        int selectedBgColor = helper.itemView.getResources().getColor(isSelected ? R.color.color_f5f5f5 : R.color.color_ffffff);
        helper.itemView.setBackgroundColor(selectedBgColor);

        helper.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick(helper.getAdapterPosition());
            }
        });
    }

    @Override
    public void setNewData(@Nullable List<ColumnEntity> data) {
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
        void onClick(int pos);
    }

}
