package com.fy.fayou.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.home.adapter.WantedAdapter;
import com.fy.fayou.home.bean.WantedEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

public class WantedFragment extends BaseListFragment<WantedEntity> {

    private static final String ARG_NAME = "arg_name";
    private static final String ARG_TYPE = "arg_type";
    private static final String ARG_POSITION = "arg_position";
    private String type;
    private String name;
    private String position;

    RecyclerView mRecyclerView;
    WantedAdapter mAdapter;

    public static WantedFragment newInstance(String type, String name, String position) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        args.putString(ARG_NAME, name);
        args.putString(ARG_POSITION, position);
        WantedFragment fragment = new WantedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString(ARG_TYPE, "A级通缉");
            name = bundle.getString(ARG_NAME, "");
            position = bundle.getString(ARG_POSITION, "");
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
    protected MeiBaseAdapter<WantedEntity> getAdapter() {
        mAdapter = new WantedAdapter();
        return mAdapter;
    }


    @Override
    protected Observable<Result<List<WantedEntity>>> getListObservable(int pageNo) {
        HashMap<String, String> hm = new HashMap();
        if (!TextUtils.isEmpty(name)) {
            hm.put("name", name);
        }
        if (!TextUtils.isEmpty(position)) {
            hm.put("position", position);
        }
        hm.put("type", type);
        hm.put("size", "20");
        hm.put("page", (pageNo - 1) + "");
        Observable<String> observable = EasyHttp.get(ApiUrl.CRIMINAL_FIND_LIST)
                .params(hm)
                .execute(String.class);
        return getListByField(observable, "content", WantedEntity.class);
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
