package com.fy.fayou.forum.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.OnScrollClashListener;
import com.fy.fayou.event.ForumNewTabEvent;
import com.fy.fayou.forum.adapter.ForumListAdapter;
import com.fy.fayou.forum.bean.ForumEntity;
import com.fy.fayou.view.HomeClashRecyclerView;
import com.meis.base.mei.adapter.BaseMultiAdapter;
import com.meis.base.mei.base.BaseMultiListFragment;
import com.meis.base.mei.constant.DataConstants;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class ForumListFragment extends BaseMultiListFragment<ForumEntity> {

    private HomeClashRecyclerView mRecyclerView;
    private OnScrollClashListener mListener;
    private ForumListAdapter mAdapter;

    private int mTabPosition;
    private ArrayList<ForumEntity> mTopArray;
    private String mId;

    public static ForumListFragment newInstance(int position, String id, ArrayList<ForumEntity> topArray) {
        Bundle args = new Bundle();
        ForumListFragment fragment = new ForumListFragment();
        args.putInt(Constant.Param.POSITION, position);
        args.putString(Constant.Param.ID, id);
        args.putParcelableArrayList(Constant.Param.LIST, topArray);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mTabPosition = getArguments().getInt(Constant.Param.POSITION);
            mTopArray = getArguments().getParcelableArrayList(Constant.Param.LIST);
            mId = getArguments().getString(Constant.Param.ID);
        }
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
    protected BaseMultiAdapter<ForumEntity> getAdapter() {
        return mAdapter = new ForumListAdapter();
    }

    @Override
    protected Observable<Result<List<ForumEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.GET_FORUM_LIST)
                .params("boardId", mId)
                .params("type", mTabPosition + "")
                .params("size", "20")
                .params("page", (pageNo - 1) + "")
                .execute(String.class);
        if (mTabPosition == 0 && pageNo == 1) {
            List<ForumEntity> appendList = new ArrayList<>();
            for (ForumEntity forum : mTopArray) {
                forum.helperIsTop = true;
                forum.itemType = 1;
                appendList.add(forum);
            }
            return appendListByField(observable, "content", ForumEntity.class, appendList);
        }
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
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }


    public ForumListFragment setOnScrollClashListener(OnScrollClashListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    protected boolean loadOnShow() {
        return false;
    }

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    /**
     * 发帖成功
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onForumNewTabEvent(ForumNewTabEvent event) {
        if (event != null && mTabPosition == 1 && mAdapter != null) {
            mRecyclerView.scrollToPosition(0);
            loadPage(DataConstants.FIRST_PAGE);
        }
    }

}
