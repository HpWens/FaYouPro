package com.fy.fayou.home.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.bean.City;

import java.util.ArrayList;

public class CitySecondLevelAdapter extends BaseQuickAdapter<City, BaseViewHolder> {

    public CitySecondLevelAdapter() {
        super(R.layout.item_city_second_level, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, City item) {
        helper.setText(R.id.tv_name, item.name);
    }
}
