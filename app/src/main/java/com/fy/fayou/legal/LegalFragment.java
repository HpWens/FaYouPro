package com.fy.fayou.legal;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.legal.adapter.LegalAdapter;
import com.fy.fayou.legal.bean.LegalEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import java.util.List;

import io.reactivex.Observable;

public class LegalFragment extends BaseListFragment<LegalEntity> {

    RecyclerView mRecyclerView;
    LegalAdapter mAdapter;

    private String type;

    public static LegalFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(Constant.Param.TYPE, type);
        LegalFragment fragment = new LegalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString(Constant.Param.TYPE, "");
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
    protected MeiBaseAdapter<LegalEntity> getAdapter() {
        mAdapter = new LegalAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<LegalEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.FIND_LEGAL_LIST)
                .params("type", type)
                .params("size", "20")
                .params("page", (pageNo - 1) + "")
                .execute(String.class);
        return getListByField(observable, "content", LegalEntity.class);
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
    }
}
