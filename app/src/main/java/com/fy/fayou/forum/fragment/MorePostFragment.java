package com.fy.fayou.forum.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.FYApplication;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.forum.adapter.MorePostAdapter;
import com.fy.fayou.forum.bean.ForumEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import java.util.List;

import io.reactivex.Observable;

public class MorePostFragment extends BaseListFragment<ForumEntity> {

    private RecyclerView mRecyclerView;
    private MorePostAdapter mAdapter;

    private String mKeyword;

    public static MorePostFragment newInstance(String keyword) {
        Bundle args = new Bundle();
        MorePostFragment fragment = new MorePostFragment();
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
    protected MeiBaseAdapter<ForumEntity> getAdapter() {
        return mAdapter = new MorePostAdapter(mKeyword);
    }

    @Override
    protected Observable<Result<List<ForumEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.GET_MORE_SEARCH_RESULT)
                .params("keyword", mKeyword)
                .params("userId", ((FYApplication) getActivity().getApplication()).getUUID())
                .params("size", "20")
                .params("mode", pageNo == 1 ? "0" : "1")
                .params("module", 7 + "")
                .execute(String.class);
        return getListByField(observable, "data", ForumEntity.class);
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
