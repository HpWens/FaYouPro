package com.fy.fayou.search.result;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.search.adapter.ResultContentAdapter;
import com.fy.fayou.search.bean.SearchResultEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


public class ContentFragment extends BaseListFragment<SearchResultEntity> {

    private static final String ARG_MENU = "arg_menu";

    RecyclerView mRecyclerView;
    ResultContentAdapter mAdapter;

    public static ContentFragment newInstance(String menu) {

        Bundle args = new Bundle();
        args.putString(ARG_MENU, menu);

        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean onBackPressedSupport() {
        return super.onBackPressedSupport();
    }


    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRecyclerView;
    }

    @Override
    protected MeiBaseAdapter<SearchResultEntity> getAdapter() {
        mAdapter = new ResultContentAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<SearchResultEntity>>> getListObservable(int pageNo) {
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
        return R.layout.comm_f5_recycler;
    }

    @Override
    protected void initData() {
        super.initData();

        List<SearchResultEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            SearchResultEntity entity = new SearchResultEntity();
            list.add(entity);
        }

        mAdapter.setNewData(list);
    }
}
