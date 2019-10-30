package com.fy.fayou.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.my.adapter.PublishAdapter;
import com.fy.fayou.my.bean.PublishEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class PublishFragment extends BaseListFragment<PublishEntity> {

    RecyclerView mRecyclerView;
    PublishAdapter mAdapter;

    public static PublishFragment newInstance() {
        Bundle args = new Bundle();
        PublishFragment fragment = new PublishFragment();
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
    protected MeiBaseAdapter<PublishEntity> getAdapter() {
        mAdapter = new PublishAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<PublishEntity>>> getListObservable(int pageNo) {
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

        List<PublishEntity> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            PublishEntity entity = new PublishEntity();
            list.add(entity);
        }
        mAdapter.setNewData(list);
    }
}
