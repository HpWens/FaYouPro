package com.fy.fayou.home.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.bean.City;
import com.fy.fayou.view.radius.RadiusLinearLayout;
import com.meis.base.mei.adapter.BaseMultiAdapter;

import java.util.ArrayList;

public class CitySecondLevelAdapter extends BaseMultiAdapter<City> {

    OnSelectListener mListener;

    public CitySecondLevelAdapter(OnSelectListener listener) {
        super(new ArrayList<>());
        mListener = listener;
        addItemType(City.NORMAL_ROW, R.layout.item_city_second_level);
        addItemType(City.FULL_ROW, R.layout.item_city_second_level2);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, City item) {
        switch (helper.getItemViewType()) {
            case City.NORMAL_ROW:
                helper.setText(R.id.tv_name, item.name)
                        .setGone(R.id.iv_pos, item.isSelect);
                RadiusLinearLayout posLayout = helper.getView(R.id.pos_layout);
                posLayout.getDelegate().setBackgroundColor(posLayout.getResources().getColor(item.isSelect ? R.color.color_1f_ed4040 : R.color.color_f5f5f5));
                helper.getView(R.id.pos_layout).setOnClickListener(v -> {
                    mListener.onSelect(v, item);
                });
                break;
            case City.FULL_ROW:
                helper.setText(R.id.tv_name, item.name);
                break;
            default:
        }
    }

    public interface OnSelectListener {
        void onSelect(View v, City item);
    }
}
