package com.fy.fayou.legal;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.legal.adapter.LegalAdapter;
import com.fy.fayou.legal.bean.LegalEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class LegalFragment extends BaseListFragment<LegalEntity> {

    RecyclerView mRecyclerView;
    LegalAdapter mAdapter;

    public static LegalFragment newInstance() {
        Bundle args = new Bundle();
        LegalFragment fragment = new LegalFragment();
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
    protected MeiBaseAdapter<LegalEntity> getAdapter() {
        mAdapter = new LegalAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<LegalEntity>>> getListObservable(int pageNo) {
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

        List<LegalEntity> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            LegalEntity entity = new LegalEntity();
            list.add(entity);
        }
        mAdapter.setNewData(list);
    }
}
