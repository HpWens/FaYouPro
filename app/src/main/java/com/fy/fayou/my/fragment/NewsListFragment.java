package com.fy.fayou.my.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.my.adapter.NewsListAdapter;
import com.fy.fayou.pufa.bean.NewsEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class NewsListFragment extends BaseListFragment<NewsEntity> {

    RecyclerView mRecyclerView;
    NewsListAdapter mAdapter;

    public static NewsListFragment newInstance() {
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
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
    protected MeiBaseAdapter<NewsEntity> getAdapter() {
        mAdapter = new NewsListAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<NewsEntity>>> getListObservable(int pageNo) {
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
        return R.layout.comm_transparent_recycler;
    }

    @Override
    protected void initData() {
        super.initData();

        List<NewsEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            NewsEntity entity = new NewsEntity();
            list.add(entity);
        }

        mAdapter.setNewData(list);
    }
}
