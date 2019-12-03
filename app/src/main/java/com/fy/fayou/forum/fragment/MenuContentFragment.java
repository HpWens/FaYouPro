package com.fy.fayou.forum.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.forum.adapter.MenuContentAdapter;
import com.fy.fayou.search.bean.ColumnEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.List;

import io.reactivex.Observable;

public class MenuContentFragment extends BaseListFragment<ColumnEntity> {

    private static final String ARG_MENU = "arg_menu";
    private static final String ARG_KEY = "arg_key";

    RecyclerView mRecyclerView;
    MenuContentAdapter mAdapter;

    private String mKey;
    private String mMenu;

    public static MenuContentFragment newInstance(String menu, String key) {

        Bundle args = new Bundle();
        args.putString(ARG_MENU, menu);
        args.putString(ARG_KEY, key);
        MenuContentFragment fragment = new MenuContentFragment();
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
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRecyclerView;
    }

    @Override
    protected MeiBaseAdapter<ColumnEntity> getAdapter() {
        mAdapter = new MenuContentAdapter(mKey);
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<ColumnEntity>>> getListObservable(int pageNo) {
        return null;
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
        return R.layout.comm_transparent_recycler;
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
