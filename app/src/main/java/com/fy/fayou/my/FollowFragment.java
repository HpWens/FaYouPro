package com.fy.fayou.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.fy.fayou.R;
import com.fy.fayou.bean.UserInfo;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.UserService;
import com.fy.fayou.my.adapter.FollowAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;

public class FollowFragment extends BaseListFragment<UserInfo> {

    RecyclerView mRecyclerView;
    FollowAdapter mAdapter;

    public static FollowFragment newInstance() {
        Bundle args = new Bundle();
        FollowFragment fragment = new FollowFragment();
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
        mAdapter = new FollowAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<UserInfo>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.MY_FOLLOW)
                .params("userId", "" + UserService.getInstance().getUserId())
                .params("page", (pageNo - 1) + "")
                .params("size", "20")
                .execute(String.class);
        return observable.map(s -> {
            Result<List<UserInfo>> result = new Result<>();
            if (!TextUtils.isEmpty(s)) {
                JSONObject json = new JSONObject(s);
                if (json.has("content")) {
                    List<UserInfo> list = new Gson().fromJson(json.optString("content"), new TypeToken<List<UserInfo>>() {
                    }.getType());
                    result.data = list;
                }
            }
            return result;
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
