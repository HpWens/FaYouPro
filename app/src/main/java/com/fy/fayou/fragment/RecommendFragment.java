package com.fy.fayou.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.fy.fayou.R;
import com.fy.fayou.adapter.RecommendAdapter;
import com.fy.fayou.bean.RecommendEntity;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.OnScrollClashListener;
import com.fy.fayou.common.UserService;
import com.fy.fayou.event.HomeRefreshEvent;
import com.fy.fayou.event.ListPraiseEvent;
import com.fy.fayou.event.LoginSuccessOrExitEvent;
import com.fy.fayou.view.HomeClashRecyclerView;
import com.meis.base.mei.adapter.BaseMultiAdapter;
import com.meis.base.mei.base.BaseMultiListFragment;
import com.meis.base.mei.constant.DataConstants;
import com.meis.base.mei.entity.Result;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

public class RecommendFragment extends BaseMultiListFragment<RecommendEntity> {

    private HomeClashRecyclerView mRecyclerView;
    private RecommendAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private OnScrollClashListener mListener;

    private String categoryId = "";

    private boolean fixedColumn = false;

    private boolean isUserCenter = false;

    private String userId = "";

    public static RecommendFragment newInstance() {
        return newInstance("");
    }

    public static RecommendFragment newInstance(String categoryId) {
        return newInstance(categoryId, false, false, "");
    }

    public static RecommendFragment newInstance(String categoryId, boolean fixedColumn) {
        return newInstance(categoryId, fixedColumn, false, "");
    }

    public static RecommendFragment newInstance(String categoryId, boolean fixedColumn, boolean isUserCenter, String userId) {
        Bundle args = new Bundle();
        args.putString(Constant.Param.CATEGORY_ID, categoryId);
        args.putBoolean(Constant.Param.FIXED_COLUMN, fixedColumn);
        args.putBoolean(Constant.Param.USER_CENTER, isUserCenter);
        args.putString(Constant.Param.USER_ID, userId);
        RecommendFragment fragment = new RecommendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        super.initData();
        if (!fixedColumn) {
            mRecyclerView.setCanScrollVertically(true);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        if (getArguments() != null) {
            categoryId = getArguments().getString(Constant.Param.CATEGORY_ID, "");
            fixedColumn = getArguments().getBoolean(Constant.Param.FIXED_COLUMN, false);
            isUserCenter = getArguments().getBoolean(Constant.Param.USER_CENTER, false);
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
        mRecyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(getActivity()));
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals("video")
                            && (position < firstVisibleItem || position > lastVisibleItem)) {

                        //如果滑出去了上面和下面就是否，和今日头条一样
                        //是否全屏
                        if (!GSYVideoManager.isFullState(getActivity())) {
                            GSYVideoManager.releaseAllVideos();
                            mAdapter.notifyDataSetChanged();
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
        mAdapter.setOnItemListener(new RecommendAdapter.OnItemListener() {
            @Override
            public void onClick(View v, int position, RecommendEntity item) {
                ARoute.jumpDetail(item.id, position, item.articleType);

                // 新增浏览记录
                HashMap<String, String> params = new HashMap<>();
                params.put("businessId", item.id);
                params.put("browseRecordType", item.articleType);
                JSONObject jsonObject = new JSONObject(params);
                EasyHttp.post(ApiUrl.MY_HISTORY)
                        .upJson(jsonObject.toString())
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onError(ApiException e) {
                            }

                            @Override
                            public void onSuccess(String s) {
                            }
                        });
            }

            @Override
            public void onPraise(View v, int position, RecommendEntity entity) {
                if (UserService.getInstance().checkLoginAndJump()) {
                    requestPraise(entity.id, position, entity);
                }
            }
        });
        return mAdapter;
    }

    /**
     * 请求点赞
     *
     * @param id
     * @param item
     */
    private void requestPraise(String id, int position, RecommendEntity item) {
        EasyHttp.post(ApiUrl.ARTICLE_PRAISE + item.id + "/give")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        if (item.give) {
                            item.gives -= 1;
                        } else {
                            item.gives += 1;
                        }
                        item.give = !item.give;
                        mAdapter.notifyItemChanged(position);
                    }
                });
    }

    @Override
    protected Observable<Result<List<RecommendEntity>>> getListObservable(int pageNo) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("page", (pageNo - 1) + "");
        hm.put("size", "20");
        hm.put("categoryId", categoryId);
        if (isUserCenter) {
            hm.put("userId", userId);
        }
        Observable<String> observable = EasyHttp.get(ApiUrl.HOME_ARTICLE)
                .params(hm)
                .execute(String.class);
        return getListByField(observable, "content", RecommendEntity.class);
    }

    @Override
    protected void onDataLoaded(int pageNo, Result<List<RecommendEntity>> result) {
        super.onDataLoaded(pageNo, result);

        // 新增全网通缉
//        if (fixedColumn && pageNo == 1) {
//            RecommendEntity entity = new RecommendEntity();
//            entity.showIndex = true;
//            entity.fullTitle = "#全国网上追逃#";
//            entity.source = "法友";
//            entity.id = "";
//            entity.fixedMode = 1;
//            mAdapter.getData().add(0, entity);
//
//            entity = new RecommendEntity();
//            entity.showIndex = true;
//            entity.fullTitle = "#我和我的祖国#";
//            entity.source = "法友";
//            entity.id = "";
//            entity.fixedMode = 2;
//            mAdapter.getData().add(0, entity);
//        }
    }

    @Override
    public boolean canLoadMore() {
        return true;
    }

    @Override
    public boolean canPullToRefresh() {
        return !fixedColumn;
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
        if (GSYVideoManager.backFromWindowFull(getActivity())) {
            return true;
        }
        return super.onBackPressedSupport();
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
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

    /**
     * 登录成功刷新接口
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccessEvent(LoginSuccessOrExitEvent event) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(0);
        }
        loadPage(DataConstants.FIRST_PAGE);
    }

    /**
     * 列表点赞事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPraiseEvent(ListPraiseEvent event) {
        if (mAdapter != null) {
            if (event.position >= 0 && mAdapter.getData().size() > event.position
                    && mAdapter.getData().get(event.position).id.equals(event.id)) {
                mAdapter.getData().get(event.position).give = event.isPraise;
                mAdapter.notifyItemChanged(event.position);
            }
        }
    }

}
