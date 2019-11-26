package com.fy.fayou.contract;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.contract.adapter.TemplateAdapter;
import com.fy.fayou.contract.bean.TemplateEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import java.util.List;

import io.reactivex.Observable;

public class TemplateFragment extends BaseListFragment<TemplateEntity> {

    RecyclerView mRecyclerView;
    TemplateAdapter mAdapter;
    String mType = "1";

    public static TemplateFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(Constant.Param.TYPE, type);
        TemplateFragment fragment = new TemplateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mType = getArguments().getString(Constant.Param.TYPE, "1");
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
    protected MeiBaseAdapter<TemplateEntity> getAdapter() {
        mAdapter = new TemplateAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<TemplateEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.GET_TEMPLATE_LIST)
                .params("type", mType)
                .params("size", "20")
                .params("page", (pageNo - 1) + "")
                .execute(String.class);
        return getListByField(observable, "content", TemplateEntity.class);
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

    @Override
    protected boolean loadOnShow() {
        return false;
    }
}
