package com.fy.fayou.forum.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.forum.adapter.MoreBoardAdapter;
import com.fy.fayou.forum.bean.PlateEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import java.util.List;

import io.reactivex.Observable;

public class MoreBoardFragment extends BaseListFragment<PlateEntity> {

    private RecyclerView mRecyclerView;
    private MoreBoardAdapter mAdapter;
    private String mKeyword;

    public static MoreBoardFragment newInstance(String keyword) {
        Bundle args = new Bundle();
        MoreBoardFragment fragment = new MoreBoardFragment();
        args.putString(Constant.Param.KEYWORD, keyword);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mKeyword = getArguments().getString(Constant.Param.KEYWORD, "");
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
    protected MeiBaseAdapter<PlateEntity> getAdapter() {
        return mAdapter = new MoreBoardAdapter(mKeyword);
    }

    @Override
    protected Observable<Result<List<PlateEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.GET_MORE_BOARD)
                .params("keyword", mKeyword)
                .params("size", "20")
                .params("page", (pageNo - 1) + "")
                .execute(String.class);
        return getListByField(observable, "content", PlateEntity.class);
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
    protected boolean loadOnShow() {
        return false;
    }
}
