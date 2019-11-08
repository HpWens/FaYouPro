package com.fy.fayou.search.result;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.search.adapter.ResultContentAdapter;
import com.fy.fayou.search.bean.SearchResultEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import java.util.List;

import io.reactivex.Observable;


public class ContentFragment extends BaseListFragment<SearchResultEntity> {

    private static final String ARG_MENU = "arg_menu";
    private static final String ARG_KEY = "arg_key";

    RecyclerView mRecyclerView;
    ResultContentAdapter mAdapter;

    private String mKey;
    private String mMenu;

    public static ContentFragment newInstance(String menu, String key) {

        Bundle args = new Bundle();
        args.putString(ARG_MENU, menu);
        args.putString(ARG_KEY, key);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mKey = bundle.getString(ARG_KEY);
            mMenu = bundle.getString(ARG_MENU);
        }
        super.initView();
    }

    @Override
    public boolean onBackPressedSupport() {
        return super.onBackPressedSupport();
    }


    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRecyclerView;
    }

    @Override
    protected MeiBaseAdapter<SearchResultEntity> getAdapter() {
        mAdapter = new ResultContentAdapter(mKey);
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<SearchResultEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(pageNo == 1 ? getRequestFooter() : getRequestMoreFooter())
                .params("userId", "" + UserService.getInstance().getUserId())
                .params("keyword", mKey)
                .baseUrl(Constant.BASE_URL6)
                .execute(String.class);
        return getListByField(observable, "data", SearchResultEntity.class);
    }

    @Override
    public boolean canLoadMore() {
        return true;
    }

    @Override
    public boolean canPullToRefresh() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_f5_recycler;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private String getRequestFooter() {
        switch (mMenu) {
            default:
            case "legalProvisions":
                return ApiUrl.SEARCH_LEGAL;
            case "judicialInterpretation":
                return ApiUrl.SEARCH_JUDICIAL;
            case "newInfo":
                return ApiUrl.SEARCH_NEWS;
            case "judgement":
                return ApiUrl.SEARCH_JUDGEMENT;
            case "caseInfo":
                return ApiUrl.SEARCH_CASE_INFO;
        }
    }

    private String getRequestMoreFooter() {
        switch (mMenu) {
            default:
            case "legalProvisions":
                return ApiUrl.SEARCH_LEGAL_MORE;
            case "judicialInterpretation":
                return ApiUrl.SEARCH_JUDICIAL_MORE;
            case "newInfo":
                return ApiUrl.SEARCH_NEWS_MORE;
            case "judgement":
                return ApiUrl.SEARCH_JUDGEMENT_MORE;
            case "caseInfo":
                return ApiUrl.SEARCH_CASE_INFO_MORE;
        }
    }


}
