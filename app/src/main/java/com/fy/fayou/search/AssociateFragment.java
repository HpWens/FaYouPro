package com.fy.fayou.search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.search.adapter.AssociateAdapter;
import com.fy.fayou.search.bean.SearchEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class AssociateFragment extends BaseListFragment<SearchEntity> {

    RecyclerView recyclerView;
    AssociateAdapter associateAdapter;

    public static AssociateFragment newInstance() {
        Bundle args = new Bundle();
        AssociateFragment fragment = new AssociateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView getRecyclerView() {
        recyclerView = getView().findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    @Override
    protected MeiBaseAdapter<SearchEntity> getAdapter() {
        associateAdapter = new AssociateAdapter();
        return associateAdapter;
    }

    @Override
    protected Observable<Result<List<SearchEntity>>> getListObservable(int pageNo) {
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
        return R.layout.comm_recycler;
    }

    @Override
    protected void initData() {
        super.initData();
        List<SearchEntity> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            SearchEntity entity = new SearchEntity();
            list.add(entity);
        }
        associateAdapter.setNewData(list);
    }
}
