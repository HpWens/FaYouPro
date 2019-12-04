package com.fy.fayou.forum.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.forum.activity.SelectPlateActivity;
import com.fy.fayou.forum.adapter.MenuContentAdapter;
import com.fy.fayou.forum.bean.PlateEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class MenuContentFragment extends BaseListFragment<PlateEntity> {

    private static final String ARG_ID = "arg_id";

    RecyclerView mRecyclerView;
    MenuContentAdapter mAdapter;

    private String mParentId;

    private List<PlateEntity> selectedArray = new ArrayList<>();

    public static MenuContentFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        MenuContentFragment fragment = new MenuContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mParentId = bundle.getString(ARG_ID);
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
        mAdapter = new MenuContentAdapter(mParentId, selectedArray);
        mAdapter.setOnItemClickListener((MenuContentAdapter.OnItemClickListener) (view, pos, selectedArray) -> {
            this.selectedArray = selectedArray;
            if (getActivity() instanceof SelectPlateActivity) {
                ((SelectPlateActivity) getActivity()).setSelectedPlate(selectedArray);
            }

        });
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<PlateEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.FORUM_MY_FOLLOW)
                .params("parentId", mParentId)
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
