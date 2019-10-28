package com.fy.fayou.search.result;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.fy.fayou.R;
import com.meis.base.mei.base.BaseFragment;


public class ContentFragment extends BaseFragment {

    private static final String ARG_MENU = "arg_menu";

    private TextView mTvContent;
    private Button mBtnNext;

    private String mMenu;

    public static ContentFragment newInstance(String menu) {

        Bundle args = new Bundle();
        args.putString(ARG_MENU, menu);

        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mMenu = args.getString(ARG_MENU);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        return super.onBackPressedSupport();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mTvContent = (TextView) getView().findViewById(R.id.tv_content);
        mBtnNext = (Button) getView().findViewById(R.id.btn_next);

        mTvContent.setText("Content:\n" + mMenu);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_result_content;
    }
}
