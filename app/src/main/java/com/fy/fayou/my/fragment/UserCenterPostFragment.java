package com.fy.fayou.my.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.OnScrollClashListener;
import com.fy.fayou.forum.bean.ForumEntity;
import com.fy.fayou.my.adapter.PostAdapter;
import com.fy.fayou.view.HomeClashRecyclerView;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import java.util.List;

import io.reactivex.Observable;

public class UserCenterPostFragment extends BaseListFragment<ForumEntity> {

    private HomeClashRecyclerView mRecyclerView;

    private String userId = "";

    private OnScrollClashListener mListener;

    private PostAdapter mAdapter;

    public static UserCenterPostFragment newInstance(String userId) {
        Bundle args = new Bundle();
        args.putString(Constant.Param.USER_ID, userId);
        UserCenterPostFragment fragment = new UserCenterPostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        if (getArguments() != null) {
            userId = getArguments().getString(Constant.Param.USER_ID, "0");
        }
        super.onAttach(activity);
    }

    @Override
    protected void initView() {
        super.initView();
        mRecyclerView.setOnScrollClashListener(isScroll -> {
            if (mListener != null) {
                mListener.onScroll(isScroll);
            }
        });
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRecyclerView;
    }

    @Override
    protected MeiBaseAdapter<ForumEntity> getAdapter() {
        return mAdapter = new PostAdapter(false);
    }

    @Override
    protected Observable<Result<List<ForumEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.GET_USER_POST)
                .params("userId", userId)
                .execute(String.class);
        return getListByField(observable, "content", ForumEntity.class);
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
    protected boolean loadOnShow() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    public UserCenterPostFragment setOnScrollClashListener(OnScrollClashListener listener) {
        mListener = listener;
        return this;
    }
}
