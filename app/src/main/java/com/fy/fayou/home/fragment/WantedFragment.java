package com.fy.fayou.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.home.adapter.WantedAdapter;
import com.fy.fayou.home.bean.WantedEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class WantedFragment extends BaseListFragment<WantedEntity> {

    RecyclerView mRecyclerView;
    WantedAdapter mAdapter;

    public static WantedFragment newInstance() {
        Bundle args = new Bundle();
        WantedFragment fragment = new WantedFragment();
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
    protected MeiBaseAdapter<WantedEntity> getAdapter() {
        mAdapter = new WantedAdapter();
        return mAdapter;
    }


    @Override
    protected Observable<Result<List<WantedEntity>>> getListObservable(int pageNo) {
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

        List<WantedEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            WantedEntity entity = new WantedEntity();
            list.add(entity);
        }

        mAdapter.setNewData(list);
    }
}
