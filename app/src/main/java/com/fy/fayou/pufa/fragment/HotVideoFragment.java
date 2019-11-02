package com.fy.fayou.pufa.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.pufa.adapter.HotVideoAdapter;
import com.fy.fayou.pufa.bean.HotVideoEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class HotVideoFragment extends BaseListFragment<HotVideoEntity> {

    RecyclerView mRecyclerView;
    HotVideoAdapter mAdapter;

    public static HotVideoFragment newInstance() {
        Bundle args = new Bundle();
        HotVideoFragment fragment = new HotVideoFragment();
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
    protected MeiBaseAdapter<HotVideoEntity> getAdapter() {
        mAdapter = new HotVideoAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<HotVideoEntity>>> getListObservable(int pageNo) {
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

        List<HotVideoEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            HotVideoEntity entity = new HotVideoEntity();
            list.add(entity);
        }

        mAdapter.setNewData(list);
    }
}
