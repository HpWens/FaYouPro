package com.fy.fayou.my.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.my.adapter.CommentAdapter;
import com.fy.fayou.my.bean.CommentEntity;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

import io.reactivex.Observable;

public class CommentFragment extends BaseListFragment<CommentEntity> {

    RecyclerView mRecyclerView;
    CommentAdapter mAdapter;

    public static CommentFragment newInstance() {
        Bundle args = new Bundle();
        CommentFragment fragment = new CommentFragment();
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
    protected MeiBaseAdapter<CommentEntity> getAdapter() {
        mAdapter = new CommentAdapter((v, position, id) -> {
            requestDeleteComment(position, id);
        });
        return mAdapter;
    }

    private void requestDeleteComment(int position, String id) {
        EasyHttp.delete(ApiUrl.COMMENT_DELETE + id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        ParseUtils.handlerApiError(e, error -> {
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onSuccess(String s) {
                        mAdapter.notifyItemRemoved(position);
                        mAdapter.getData().remove(position);
                    }
                });
    }

    @Override
    protected Observable<Result<List<CommentEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.MY_COMMENT)
                .params("size", "20")
                .params("page", "" + (pageNo - 1))
                .execute(String.class);
        return getListByField(observable, "content", CommentEntity.class);
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
