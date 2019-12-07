package com.fy.fayou.forum.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.forum.adapter.PlateFollowAdapter;
import com.fy.fayou.forum.bean.PlateEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

public class PlateFollowFragment extends BaseListFragment<PlateEntity> {

    RecyclerView mRecyclerView;
    PlateFollowAdapter mAdapter;

    String parentId = "-1";

    public static PlateFollowFragment newInstance(String id) {
        Bundle args = new Bundle();
        PlateFollowFragment fragment = new PlateFollowFragment();
        args.putString(Constant.Param.ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            parentId = getArguments().getString(Constant.Param.ID, "-1");
        }
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        return mRecyclerView;
    }

    @Override
    protected MeiBaseAdapter<PlateEntity> getAdapter() {
        mAdapter = new PlateFollowAdapter(new PlateFollowAdapter.OnItemListener() {
            @Override
            public void onFollow(View view, int position, PlateEntity item) {
                followPlate(position, item);
            }

            @Override
            public void onItem(View view, PlateEntity item) {

            }
        });
        return mAdapter;
    }

    /**
     * @param position
     * @param item
     */
    private void followPlate(int position, PlateEntity item) {

        HashMap<String, String> params = new HashMap<>();
        params.put("boardId", item.id);
        params.put("followed", !item.followed + "");
        JSONObject jsonObject = new JSONObject(params);

        EasyHttp.put(ApiUrl.FORUM_FOLLOW_PLATE)
                .upJson(jsonObject.toString())
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        item.followed = !item.followed;
                        mAdapter.notifyItemChanged(position);
                    }
                });
    }

    @Override
    protected Observable<Result<List<PlateEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.FORUM_MY_FOLLOW)
                .params("parentId", parentId)
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
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_recycler;
    }
}
