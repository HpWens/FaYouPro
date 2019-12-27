package com.fy.fayou.search.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.fy.fayou.FYApplication;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.search.adapter.MoreResultAdapter;
import com.fy.fayou.search.bean.SearchResultEntity;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.adapter.BaseMultiAdapter;
import com.meis.base.mei.base.BaseMultiListFragment;
import com.meis.base.mei.entity.Result;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.zhouyou.http.EasyHttp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class MoreSearchResultFragment extends BaseMultiListFragment<SearchResultEntity> {

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;
    private MoreResultAdapter mAdapter;

    private int mType;
    private String mKeyword;

    public static MoreSearchResultFragment newInstance(int type, String keyword) {
        Bundle args = new Bundle();
        MoreSearchResultFragment fragment = new MoreSearchResultFragment();
        args.putInt(Constant.Param.TYPE, type);
        args.putString(Constant.Param.KEYWORD, keyword);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mType = getArguments().getInt(Constant.Param.TYPE);
            mKeyword = getArguments().getString(Constant.Param.KEYWORD);
        }
        super.initView();
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(getActivity()));

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
    protected BaseMultiAdapter<SearchResultEntity> getAdapter() {
        return mAdapter = new MoreResultAdapter(mKeyword);
    }


    @Override
    protected Observable<Result<List<SearchResultEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.GET_MORE_SEARCH_RESULT)
                .params("userId", ((FYApplication) getActivity().getApplication()).getUUID())
                .params("keyword", mKeyword)
                .params("size", "20")
                .params("mode", pageNo == 1 ? "0" : "1")
                .params("module", mType + "")
                .execute(String.class);
        return getListByField(observable);
    }

    /**
     * @param observable
     * @return
     */
    protected Observable<Result<List<SearchResultEntity>>> getListByField(Observable<String> observable) {
        final Result<List<SearchResultEntity>> result = new Result<>();
        return observable.map(s -> {
            if (!TextUtils.isEmpty(s)) {
                JSONObject json = new JSONObject(s);
                if (json != null && json.has("data")) {
                    List<SearchResultEntity> list = ParseUtils.parseListData(json.optString("data"), SearchResultEntity.class);
                    result.data = new ArrayList<>();
                    for (SearchResultEntity entity : list) {
                        entity.columnType = mType;
                        entity.itemType = getItemType(mType, entity.articleType);
                        result.data.add(entity);
                    }
                }
            }
            return result;
        });
    }

    /**
     * * 模块标识      * 1 法律      * 2 司法解释      * 3 裁判文书      * 4 指导性案例      * 5 合同模板      * 6 普法天地  * 7论坛帖子
     *
     * @param type
     * @return
     */
    private int getItemType(int type, String articleType) {
        int itemType = 0;
        switch (type) {
            case 1:
            case 2:
            case 3:
            case 4:
                itemType = 0;
                break;
            case 5:
                itemType = 1;
                break;
            case 6:
                if (articleType != null && articleType.equals("VIDEO")) {
                    itemType = 3;
                } else {
                    itemType = 2;
                }
                break;
        }
        return itemType;
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
    protected boolean loadOnShow() {
        return false;
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
    public boolean onBackPressedSupport() {
        if (GSYVideoManager.backFromWindowFull(getActivity())) {
            return true;
        }
        return super.onBackPressedSupport();
    }
}
