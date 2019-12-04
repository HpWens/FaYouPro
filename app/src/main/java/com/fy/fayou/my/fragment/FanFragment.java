package com.fy.fayou.my.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fy.fayou.R;
import com.fy.fayou.bean.UserInfo;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.UserService;
import com.fy.fayou.my.adapter.FanAdapter;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

import io.reactivex.Observable;

public class FanFragment extends BaseListFragment<UserInfo> {

    RecyclerView mRecyclerView;
    FanAdapter mAdapter;

    public static FanFragment newInstance() {
        Bundle args = new Bundle();
        FanFragment fragment = new FanFragment();
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
    protected MeiBaseAdapter<UserInfo> getAdapter() {
        mAdapter = new FanAdapter(new FanAdapter.OnItemListener() {
            @Override
            public void onFollow(UserInfo user, int position) {
                requestFollow(user, position);
            }

            @Override
            public void onUnFollow(UserInfo user, int position) {
                cancelFollow(user, position);
            }

            @Override
            public void onJumpUserCenter(View v, String id) {
                ARoute.jumpUserCenter(id);
            }
        });
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<UserInfo>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.USER_FAN)
                .params("userId", "" + UserService.getInstance().getUserId())
                .params("page", (pageNo - 1) + "")
                .params("size", "20")
                .execute(String.class);
        return getListByField(observable, "content", UserInfo.class);
    }

    // 取消关注
    private void cancelFollow(final UserInfo user, final int position) {
        EasyHttp.post(ApiUrl.USER_CANCEL_FOLLOW)
                .upJson(user.id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        user.follow = false;
                        mAdapter.setData(position, user);
                    }
                });
    }

    // 请求关注
    private void requestFollow(final UserInfo user, final int position) {
        EasyHttp.post(ApiUrl.USER_REQUEST_FOLLOW)
                .upJson(user.id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        user.follow = true;
                        mAdapter.setData(position, user);
                    }
                });
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
}