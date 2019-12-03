package com.fy.fayou.forum.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.common.OnScrollClashListener;
import com.fy.fayou.forum.bean.ForumEntity;
import com.fy.fayou.view.HomeClashRecyclerView;
import com.meis.base.mei.adapter.BaseMultiAdapter;
import com.meis.base.mei.base.BaseMultiListFragment;
import com.meis.base.mei.entity.Result;

import java.util.List;

import io.reactivex.Observable;

public class ForumListFragment extends BaseMultiListFragment<ForumEntity> {

    private HomeClashRecyclerView mRecyclerView;
    private OnScrollClashListener mListener;

    public static ForumListFragment newInstance() {
        Bundle args = new Bundle();
        ForumListFragment fragment = new ForumListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        mRecyclerView.setOnScrollClashListener(isScroll -> {
            if (mListener != null) {
                mListener.onScroll(isScroll);
            }
        });
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRecyclerView;
    }

    @Override
    protected BaseMultiAdapter<ForumEntity> getAdapter() {
        return null;
    }

    @Override
    protected Observable<Result<List<ForumEntity>>> getListObservable(int pageNo) {
        return null;
    }

    @Override
    public boolean canLoadMore() {
        return false;
    }

    @Override
    public boolean canPullToRefresh() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }


    public ForumListFragment setOnScrollClashListener(OnScrollClashListener listener) {
        mListener = listener;
        return this;
    }
}
