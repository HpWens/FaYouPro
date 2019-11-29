package com.fy.fayou.forum.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.forum.adapter.PlateFollowAdapter;
import com.fy.fayou.forum.bean.PlateEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class PlateFollowFragment extends BaseListFragment<PlateEntity> {

    RecyclerView mRecyclerView;
    PlateFollowAdapter mAdapter;

    public static PlateFollowFragment newInstance() {
        Bundle args = new Bundle();
        PlateFollowFragment fragment = new PlateFollowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        super.initData();

        List<PlateEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            PlateEntity entity = new PlateEntity();
            list.add(entity);
        }
        mAdapter.setNewData(list);
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRecyclerView;
    }

    @Override
    protected MeiBaseAdapter<PlateEntity> getAdapter() {
        mAdapter = new PlateFollowAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<PlateEntity>>> getListObservable(int pageNo) {
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
}
