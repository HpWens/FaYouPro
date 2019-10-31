package com.fy.fayou.detail.article;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fy.fayou.R;
import com.fy.fayou.detail.adapter.ReviewAdapter;
import com.fy.fayou.detail.bean.Level1Item;
import com.fy.fayou.detail.bean.Level2Item;
import com.meis.base.mei.adapter.BaseMultiAdapter;
import com.meis.base.mei.base.BaseMultiListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class ReviewListFragment extends BaseMultiListFragment<MultiItemEntity> {

    private RecyclerView mRecyclerView;
    private ReviewAdapter mAdapter;

    public static ReviewListFragment newInstance() {
        Bundle args = new Bundle();
        ReviewListFragment fragment = new ReviewListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = getView().findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRecyclerView;
    }

    @Override
    protected BaseMultiAdapter<MultiItemEntity> getAdapter() {
        return mAdapter = new ReviewAdapter();
    }

    @Override
    protected Observable<Result<List<MultiItemEntity>>> getListObservable(int pageNo) {
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

        List<MultiItemEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Level1Item entity = new Level1Item();
            list.add(entity);

            if (i % 3 == 0) {
                Level2Item level2Item = new Level2Item();
                list.add(level2Item);
                level2Item = new Level2Item();
                list.add(level2Item);
                level2Item = new Level2Item();
                list.add(level2Item);
            }
        }
        mAdapter.setNewData(list);
    }
}
