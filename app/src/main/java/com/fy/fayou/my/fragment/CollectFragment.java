package com.fy.fayou.my.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.my.adapter.CollectAdapter;
import com.fy.fayou.my.bean.CollectEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class CollectFragment extends BaseListFragment<CollectEntity> {

    RecyclerView mRecyclerView;
    CollectAdapter mAdapter;

    public static CollectFragment newInstance() {
        Bundle args = new Bundle();
        CollectFragment fragment = new CollectFragment();
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
    protected MeiBaseAdapter<CollectEntity> getAdapter() {
        mAdapter = new CollectAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<CollectEntity>>> getListObservable(int pageNo) {
        return null;
    }

    @Override
    public boolean canLoadMore() {
        return false;
    }

    @Override
    public boolean canPullToRefresh() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_transparent_recycler;
    }

    @Override
    protected void initData() {
        super.initData();

        List<CollectEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            CollectEntity entity = new CollectEntity();
            list.add(entity);
        }

        mAdapter.setNewData(list);

    }
}
