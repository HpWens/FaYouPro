package com.fy.fayou.forum.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.View;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.event.LoginSuccessOrExitEvent;
import com.fy.fayou.event.RefreshFollowPlateEvent;
import com.fy.fayou.forum.adapter.FollowAdapter;
import com.fy.fayou.forum.adapter.PlateAdapter;
import com.fy.fayou.forum.bean.ForumEntity;
import com.fy.fayou.forum.bean.PlateEntity;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.constant.DataConstants;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.status.ViewState;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.Observable;

public class FollowFragment extends BaseListFragment<ForumEntity> {

    RecyclerView mRecyclerView;
    FollowAdapter mAdapter;
    PlateAdapter mPlateAdapter;

    boolean mIsRecommendColumn;

    public static FollowFragment newInstance(boolean isRecommend) {
        Bundle args = new Bundle();
        args.putBoolean(Constant.Param.IS_RECOMMEND, isRecommend);
        FollowFragment fragment = new FollowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mIsRecommendColumn = getArguments().getBoolean(Constant.Param.IS_RECOMMEND, false);
        }
        super.initView();
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        return mRecyclerView;
    }

    @Override
    protected void initData() {
        super.initData();
        if (!mIsRecommendColumn) {
            mAdapter.addHeaderView(getHeaderView());
        }
    }

    @Override
    protected MeiBaseAdapter getAdapter() {
        return mAdapter = new FollowAdapter();
    }

    @Override
    protected Observable<Result<List<ForumEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(!mIsRecommendColumn ? ApiUrl.FORUM_HOME_LIST : ApiUrl.FORUM_HOME_RECOMMEND_LIST)
                .params("size", "20")
                .params("page", (pageNo - 1) + "")
                .execute(String.class);
        return getListByField(observable, "content", ForumEntity.class);
    }

    @Override
    protected void onDataLoaded(int pageNo, Result<List<ForumEntity>> result) {
        super.onDataLoaded(pageNo, result);
        setState(ViewState.COMPLETED);
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

    private View getHeaderView() {
        View header = View.inflate(getActivity(), R.layout.forum_home_plate, null);
        RecyclerView recyclerView = header.findViewById(R.id.recycler_plate);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(mPlateAdapter = new PlateAdapter());

//        mPlateAdapter.setLoadMoreView(new HorizontalLoadMoreView());
//        mPlateAdapter.setOnLoadMoreListener(() -> {
//        }, recyclerView);

        requestHeaderData();

        return header;
    }

    private void requestHeaderData() {
        EasyHttp.get(ApiUrl.FORUM_MY_FOLLOW)
                .params("parentId", "-1")
                .params("page", "0")
                .params("size", "20")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            List<PlateEntity> list = ParseUtils.parseListData(s, "content", PlateEntity.class);

                            PlateEntity myPlate = new PlateEntity();
                            myPlate.name = "我的板块";
                            myPlate.helperIsMy = true;
                            list.add(0, myPlate);

                            mPlateAdapter.setNewData(list);
                        }
                    }
                });
    }

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccessEvent(LoginSuccessOrExitEvent event) {
        // 登陆成功后刷新-关注tab
        if (mAdapter != null && !mIsRecommendColumn) {
            requestHeaderData();
            loadPage(DataConstants.FIRST_PAGE);
        }
    }

    /**
     * 刷新关注的板块
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshFollowPlateEvent(RefreshFollowPlateEvent event) {
        // 登陆成功后刷新-关注tab
        if (mPlateAdapter != null && !mIsRecommendColumn) {
            requestHeaderData();
        }
    }

}
