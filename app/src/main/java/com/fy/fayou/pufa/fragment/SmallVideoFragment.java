package com.fy.fayou.pufa.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.pufa.adapter.SmallVideoAdapter;
import com.fy.fayou.pufa.bean.SmallVideoEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class SmallVideoFragment extends BaseListFragment<SmallVideoEntity> {

    RecyclerView mRecyclerView;
    SmallVideoAdapter mAdapter;

    public static SmallVideoFragment newInstance() {
        Bundle args = new Bundle();
        SmallVideoFragment fragment = new SmallVideoFragment();
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
    protected MeiBaseAdapter<SmallVideoEntity> getAdapter() {
        mAdapter = new SmallVideoAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<SmallVideoEntity>>> getListObservable(int pageNo) {
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

        List<SmallVideoEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            SmallVideoEntity entity = new SmallVideoEntity();
            list.add(entity);
        }

        mAdapter.setNewData(list);
    }
}
