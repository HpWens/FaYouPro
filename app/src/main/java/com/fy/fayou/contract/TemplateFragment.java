package com.fy.fayou.contract;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.contract.adapter.TemplateAdapter;
import com.fy.fayou.contract.bean.TemplateEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

public class TemplateFragment extends BaseListFragment<TemplateEntity> {

    RecyclerView mRecyclerView;
    TemplateAdapter mAdapter;

    int mCollectType;
    String mType = "1";
    String mTags;

    public static TemplateFragment newInstance(String type, String tags, int collectType) {
        Bundle args = new Bundle();
        args.putString(Constant.Param.TYPE, type);
        args.putString(Constant.Param.TAGS, tags);
        args.putInt(Constant.Param.COLLECT_TYPE, collectType);
        TemplateFragment fragment = new TemplateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mType = getArguments().getString(Constant.Param.TYPE, "1");
            mTags = getArguments().getString(Constant.Param.TAGS, "");
            mCollectType = getArguments().getInt(Constant.Param.COLLECT_TYPE, ARoute.TEMPLATE_TYPE);
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
        mAdapter = new TemplateAdapter(mCollectType);
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<TemplateEntity>>> getListObservable(int pageNo) {
        HashMap<String, String> hm = new HashMap<>();
        if (!TextUtils.isEmpty(mTags)) {
            hm.put("tag", mTags);
        }
        hm.put("type", mType);
        hm.put("size", "20");
        hm.put("page", (pageNo - 1) + "");
        Observable<String> observable = EasyHttp.get(ApiUrl.GET_TEMPLATE_LIST)
                .params(hm)
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
