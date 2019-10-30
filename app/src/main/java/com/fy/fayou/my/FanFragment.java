package com.fy.fayou.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.my.adapter.FanAdapter;
import com.fy.fayou.my.bean.FanEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class FanFragment extends BaseListFragment<FanEntity> {

    RecyclerView mRecyclerView;
    FanAdapter mAdapter;

    public static FanFragment newInstance() {
        Bundle args = new Bundle();
        FanFragment fragment = new FanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRecyclerView;
    }

    @Override
    protected MeiBaseAdapter<FanEntity> getAdapter() {
        mAdapter = new FanAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<FanEntity>>> getListObservable(int pageNo) {
        return null;
    }

    @Override
    public boolean canLoadMore() {
        return true;
    }

    @Override
    public boolean canPullToRefresh() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_recycler;
    }

    @Override
    protected void initData() {
        super.initData();

        List<FanEntity> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            FanEntity entity = new FanEntity();
            list.add(entity);
        }
        mAdapter.setNewData(list);
    }
}