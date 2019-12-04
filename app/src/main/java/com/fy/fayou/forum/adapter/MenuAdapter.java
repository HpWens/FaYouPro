package com.fy.fayou.forum.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.forum.bean.PlateEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends MeiBaseAdapter<PlateEntity> {

    private int mLastCheckedPosition = -1;
    private SparseBooleanArray mBooleanArray;
    private OnItemClickListener mListener;

    public MenuAdapter() {
        super(R.layout.item_plate_select_menu, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PlateEntity item) {

        boolean isSelected = mBooleanArray.get(helper.getAdapterPosition());

        helper.setText(R.id.tv_name, item.name)
                .setTextColor(R.id.tv_name, helper.itemView.getResources().getColor(isSelected ? R.color.color_333333 : R.color.color_a0a0a0));

        TextView tvName = helper.getView(R.id.tv_name);
        tvName.getPaint().setFakeBoldText(isSelected);

        helper.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick(helper.getAdapterPosition());
            }
        });
    }

    @Override
    public void setNewData(@Nullable List<PlateEntity> data) {
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
