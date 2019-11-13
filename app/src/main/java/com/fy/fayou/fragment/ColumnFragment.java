package com.fy.fayou.fragment;

import android.os.Bundle;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.adapter.HomeColumnVPAdapter;
import com.fy.fayou.common.OnScrollClashListener;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseFragment;
import com.meis.base.mei.status.ViewState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ColumnFragment extends BaseFragment {

    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;

    Unbinder unbinder;
    private OnScrollClashListener mListener;
    private HomeColumnVPAdapter mAdapter;

    private final String[] mTitles = {
            "新闻", "视频", "悬案", "教学", "新闻", "视频", "悬案", "教学"
    };

    public static ColumnFragment newInstance() {
        Bundle args = new Bundle();
        ColumnFragment fragment = new ColumnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, getView());

        setState(ViewState.EMPTY, "敬请期待");

//        viewpager.setAdapter(mAdapter = new HomeColumnVPAdapter(getChildFragmentManager(), mTitles));
//        tab.setViewPager(viewpager);
//
//        mAdapter.setOnScrollClashListener(isScroll -> {
//            viewpager.setScroll(isScroll);
//            if (mListener != null) {
//                mListener.onScroll(isScroll);
//            }
//        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_column;
    }


    public ColumnFragment setOnScrollClashListener(OnScrollClashListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_add)
    public void onClick() {

    }
}
