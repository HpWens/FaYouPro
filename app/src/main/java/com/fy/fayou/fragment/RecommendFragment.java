package com.fy.fayou.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fy.fayou.R;
import com.fy.fayou.adapter.RecommendAdapter;
import com.fy.fayou.bean.RecommendEntity;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.OnScrollClashListener;
import com.fy.fayou.event.HomeRefreshEvent;
import com.fy.fayou.view.HomeClashRecyclerView;
import com.meis.base.mei.adapter.BaseMultiAdapter;
import com.meis.base.mei.base.BaseMultiListFragment;
import com.meis.base.mei.constant.DataConstants;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.jzvd.Jzvd;
import io.reactivex.Observable;

public class RecommendFragment extends BaseMultiListFragment<RecommendEntity> {

    private HomeClashRecyclerView mRecyclerView;
    private RecommendAdapter mAdapter;

    private OnScrollClashListener mListener;

    public static RecommendFragment newInstance() {
        Bundle args = new Bundle();
        RecommendFragment fragment = new RecommendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        super.initData();
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
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                if (view.getTag() != null && view.getTag().toString().equals("video")) {
                    Jzvd jzvd = view.findViewById(R.id.video_player);
                    if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
                            jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                        if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                            Jzvd.releaseAllVideos();
                        }
                    }
                }
            }
        });
        return mRecyclerView;
    }

    @Override
    protected BaseMultiAdapter<RecommendEntity> getAdapter() {
        mAdapter = new RecommendAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<RecommendEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.HOME_ARTICLE)
                .params("page", (pageNo - 1) + "")
                .params("size", "20")
                .baseUrl(Constant.BASE_URL4)
                .execute(String.class);
        return getListByField(observable, "content", RecommendEntity.class);
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
        return R.layout.fragment_recommend;
    }

    public RecommendFragment setOnScrollClashListener(OnScrollClashListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public boolean onBackPressedSupport() {
        if (Jzvd.backPress()) {
            return true;
        }
        return super.onBackPressedSupport();
    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(HomeRefreshEvent event) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(0);
        }
        loadPage(DataConstants.FIRST_PAGE);
    }

    @Override
    protected boolean loadOnShow() {
        return false;
    }
}
