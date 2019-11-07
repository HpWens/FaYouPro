package com.fy.fayou.contract;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.contract.adapter.TemplateAdapter;
import com.fy.fayou.contract.bean.TemplateEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class TemplateFragment extends BaseListFragment<TemplateEntity> {

    RecyclerView mRecyclerView;
    TemplateAdapter mAdapter;

    public static TemplateFragment newInstance() {
        Bundle args = new Bundle();
        TemplateFragment fragment = new TemplateFragment();
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
    protected MeiBaseAdapter<TemplateEntity> getAdapter() {
        mAdapter = new TemplateAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<TemplateEntity>>> getListObservable(int pageNo) {
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

        List<TemplateEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            TemplateEntity entity = new TemplateEntity();
            list.add(entity);
        }

        mAdapter.setNewData(list);
    }

}
