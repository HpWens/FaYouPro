package com.fy.fayou.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.adapter.RecommendAdapter;
import com.fy.fayou.bean.RecommendEntity;
import com.fy.fayou.common.OnScrollClashListener;
import com.fy.fayou.view.HomeClashRecyclerView;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class RecommendFragment extends BaseListFragment<RecommendEntity> {

    private HomeClashRecyclerView mRecyclerView;
    private RecommendAdapter mAdapter;

    private OnScrollClashListener mListener;

    public static RecommendFragment newInstance() {
        Bundle args = new Bundle();
        RecommendFragment fragment = new RecommendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView() {
        super.initView();
        mRecyclerView.setOnScrollClashListener(isScroll -> {
            if (mListener != null) {
                mListener.onScroll(isScroll);
            }
        });
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRecyclerView;
    }

    @Override
    protected MeiBaseAdapter<RecommendEntity> getAdapter() {
        List<RecommendEntity> entityList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            RecommendEntity entity = new RecommendEntity();
            entityList.add(entity);
        }
        mAdapter = new RecommendAdapter(entityList);
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<RecommendEntity>>> getListObservable(int pageNo) {
        return null;
    }

    @Override
    public boolean canLoadMore() {
        return true;
    }

    @Override
    public boolean canPullToRefresh() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    public RecommendFragment setOnScrollClashListener(OnScrollClashListener listener) {
        mListener = listener;
        return this;
    }
}
