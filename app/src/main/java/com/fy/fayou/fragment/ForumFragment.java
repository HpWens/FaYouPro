package com.fy.fayou.fragment;

import android.os.Bundle;

import com.fy.fayou.R;
import com.meis.base.mei.base.BaseFragment;

public class ForumFragment extends BaseFragment {

    public static ForumFragment newInstance() {
        Bundle args = new Bundle();
        ForumFragment fragment = new ForumFragment();
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
        return R.layout.forum_fragment;
    }
}
