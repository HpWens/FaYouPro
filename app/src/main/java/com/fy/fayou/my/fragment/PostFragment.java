package com.fy.fayou.my.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.my.adapter.PostAdapter;
import com.fy.fayou.my.bean.PostEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class PostFragment extends BaseListFragment<PostEntity> {

    RecyclerView mRecyclerView;
    PostAdapter mAdapter;

    public static PostFragment newInstance() {
        Bundle args = new Bundle();
        PostFragment fragment = new PostFragment();
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
    protected MeiBaseAdapter<PostEntity> getAdapter() {
        mAdapter = new PostAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<PostEntity>>> getListObservable(int pageNo) {
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

        List<PostEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            PostEntity entity = new PostEntity();
            list.add(entity);
        }

        mAdapter.setNewData(list);
    }
}
