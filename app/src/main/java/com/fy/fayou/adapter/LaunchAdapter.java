package com.fy.fayou.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.bean.LaunchEntity;
import com.meis.base.mei.adapter.BaseMultiAdapter;

import java.util.List;

public class LaunchAdapter extends BaseMultiAdapter<LaunchEntity> {

    View.OnClickListener mListener;

    int[] mPicRes = {
            R.mipmap.launch2_0_ic,
            R.mipmap.launch2_1_ic,
            R.mipmap.launch2_2_ic,
            R.mipmap.launch2_3_ic,
            R.mipmap.launch2_4_ic};

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public LaunchAdapter(List<LaunchEntity> data, View.OnClickListener listener) {
        super(data);
        mListener = listener;

//        addItemType(0, R.layout.item_launch_0);
//        addItemType(1, R.layout.item_launch_1);
//        addItemType(2, R.layout.item_launch_2);

        addItemType(0, R.layout.item_launch2_0);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LaunchEntity item) {
        super.convert(helper, item);

//        if (item.itemType == 2) {
//            helper.getView(R.id.tv_enter).setOnClickListener(v -> {
//                mListener.onClick(v);
//            });
//        }

        int pos = helper.getAdapterPosition();
        helper.setImageResource(R.id.iv_launch, pos < mPicRes.length ? mPicRes[pos] : mPicRes[mPicRes.length - 1]);
        helper.itemView.setOnClickListener(v -> {
            if (pos == mPicRes.length - 1) {
                mListener.onClick(v);
            }
        });

    }
}
