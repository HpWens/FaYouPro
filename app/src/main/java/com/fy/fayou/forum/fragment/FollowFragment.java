package com.fy.fayou.forum.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fy.fayou.R;
import com.fy.fayou.common.Constant;
import com.fy.fayou.forum.adapter.FollowAdapter;
import com.fy.fayou.forum.adapter.PlateAdapter;
import com.fy.fayou.forum.bean.ForumEntity;
import com.fy.fayou.forum.bean.PlateEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class FollowFragment extends BaseListFragment<ForumEntity> {

    RecyclerView mRecyclerView;
    FollowAdapter mAdapter;
    PlateAdapter mPlateAdapter;

    boolean mIsRecommendColumn;

    public static FollowFragment newInstance(boolean isRecommend) {
        Bundle args = new Bundle();
        args.putBoolean(Constant.Param.IS_RECOMMEND, isRecommend);
        FollowFragment fragment = new FollowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mIsRecommendColumn = getArguments().getBoolean(Constant.Param.IS_RECOMMEND, false);
        }
        super.initView();
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRecyclerView;
    }

    @Override
    protected void initData() {
        super.initData();

        if (!mIsRecommendColumn) {
            mAdapter.addHeaderView(getHeaderView());
        }

        List<ForumEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ForumEntity entity = new ForumEntity();
            list.add(entity);
        }
        mAdapter.setNewData(list);
    }

    @Override
    protected MeiBaseAdapter getAdapter() {
        return mAdapter = new FollowAdapter();
    }

    @Override
    protected Observable<Result<List<ForumEntity>>> getListObservable(int pageNo) {
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

    private View getHeaderView() {
        View header = View.inflate(getActivity(), R.layout.forum_home_plate, null);
        RecyclerView recyclerView = header.findViewById(R.id.recycler_plate);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(mPlateAdapter = new PlateAdapter());

        List<PlateEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            PlateEntity entity = new PlateEntity();
            list.add(entity);
        }
        mPlateAdapter.setNewData(list);

        return header;
    }

}
