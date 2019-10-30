package com.fy.fayou.my.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.bean.UserInfo;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class FollowAdapter extends MeiBaseAdapter<UserInfo> {

    public FollowAdapter() {
        super(R.layout.item_my_follow, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, UserInfo item) {

    }
}
