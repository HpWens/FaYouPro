package com.fy.fayou.fragment;

import android.support.v7.widget.RecyclerView;

import com.fy.fayou.bean.RecommendEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;

import java.util.List;

import io.reactivex.Observable;

public class NewsFragment extends BaseListFragment<RecommendEntity> {
    @Override
    protected RecyclerView getRecyclerView() {
        return null;
    }

    @Override
    protected MeiBaseAdapter<RecommendEntity> getAdapter() {
        return null;
    }

    @Override
    protected Observable<Result<List<RecommendEntity>>> getListObservable(int pageNo) {
        return null;
    }

    @Override
    public boolean canLoadMore() {
        return false;
    }

    @Override
    public boolean canPullToRefresh() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }
}
