package com.fy.fayou.fragment;

import android.os.Bundle;

import com.fy.fayou.R;
import com.meis.base.mei.base.BaseFragment;

public class LearnFragment extends BaseFragment {

    public static LearnFragment newInstance() {
        Bundle args = new Bundle();
        LearnFragment fragment = new LearnFragment();
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
        return R.layout.learn_fragment;
    }
}
