package com.fy.fayou.my.fragment;

import android.os.Bundle;

import com.fy.fayou.R;
import com.meis.base.mei.base.BaseFragment;

public class UserCenterFragment extends BaseFragment {

    public static UserCenterFragment newInstance() {
        Bundle args = new Bundle();
        UserCenterFragment fragment = new UserCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_center;
    }
}
