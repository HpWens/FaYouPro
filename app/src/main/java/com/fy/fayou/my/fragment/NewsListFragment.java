package com.fy.fayou.my.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.my.adapter.NewsListAdapter;
import com.fy.fayou.pufa.bean.NewsEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import java.util.List;

import io.reactivex.Observable;

public class NewsListFragment extends BaseListFragment<NewsEntity> {

    RecyclerView mRecyclerView;
    NewsListAdapter mAdapter;

    private String status = "";

    public static final String ALL_STATUS = "";
    // 待审核
    public static final String ALL_SUBMIT = "SUBMIT";
    // 审核通过
    public static final String ALL_AUDIT = "AUDIT";
    // 审核失败
    public static final String ALL_FAIL = "AUDIT_FAIL";

    private static final String TYPE = "type";

    public static NewsListFragment newInstance(String auditStatus) {
        Bundle args = new Bundle();
        args.putString(TYPE, auditStatus);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = bundle.getString(TYPE);
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
    protected MeiBaseAdapter<NewsEntity> getAdapter() {
        mAdapter = new NewsListAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<NewsEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.MY_NEWS)
                .params("page", (pageNo - 1) + "")
                .params("size", "20")
                .params("auditStatus", status)
                .execute(String.class);
        return getListByField(observable, "content", NewsEntity.class);
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
        return R.layout.comm_transparent_recycler;
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
