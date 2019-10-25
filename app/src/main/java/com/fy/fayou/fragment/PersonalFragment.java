package com.fy.fayou.fragment;

import android.os.Bundle;

import com.fy.fayou.R;
import com.meis.base.mei.base.BaseFragment;

public class PersonalFragment extends BaseFragment {

    public static PersonalFragment newInstance() {
        Bundle args = new Bundle();
        PersonalFragment fragment = new PersonalFragment();
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
        return R.layout.personal_fragment;
    }
}
