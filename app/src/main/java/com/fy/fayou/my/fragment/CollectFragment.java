package com.fy.fayou.my.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.my.adapter.CollectAdapter;
import com.fy.fayou.my.bean.CollectEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import java.util.List;

import io.reactivex.Observable;

public class CollectFragment extends BaseListFragment<CollectEntity> {

    RecyclerView mRecyclerView;
    CollectAdapter mAdapter;

    private boolean isCollect = true;
    private static final String IS_COLLECT = "is_collect";
    private String categoryType = "";

    public static CollectFragment newInstance(boolean isCollect) {
        Bundle args = new Bundle();
        args.putBoolean(IS_COLLECT, isCollect);
        CollectFragment fragment = new CollectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CollectFragment newInstance(boolean isCollect, String type) {
        Bundle args = new Bundle();
        args.putBoolean(IS_COLLECT, isCollect);
        args.putString(Constant.Param.TYPE, type);
        CollectFragment fragment = new CollectFragment();
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
    protected MeiBaseAdapter<CollectEntity> getAdapter() {
        mAdapter = new CollectAdapter(isCollect);
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<CollectEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(isCollect ? ApiUrl.MY_COLLECT : ApiUrl.MY_HISTORY)
                .params("page", (pageNo - 1) + "")
                .params("size", "20")
                .params("collectType", categoryType)
                .execute(String.class);
        return getListByField(observable, "content", CollectEntity.class);
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

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            isCollect = bundle.getBoolean(IS_COLLECT);
            categoryType = bundle.getString(Constant.Param.TYPE, "");
        }
        super.initView();
    }

    @Override
    protected boolean loadOnShow() {
        return false;
    }
}
