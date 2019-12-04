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
import com.fy.fayou.forum.adapter.StarPlateAdapter;
import com.fy.fayou.forum.adapter.StarPlateHeaderAdapter;
import com.fy.fayou.forum.bean.PlateEntity;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

import io.reactivex.Observable;

public class StarPlateFragment extends BaseListFragment<PlateEntity> {

    private String id = "";
    private String name = "";

    private RecyclerView mRecyclerView;

    private StarPlateHeaderAdapter mHeaderAdapter;
    private StarPlateAdapter mAdapter;

    public static StarPlateFragment newInstance(String id, String name) {
        Bundle args = new Bundle();
        args.putString(Constant.Param.ID, id);
        args.putString(Constant.Param.NAME, name);
        StarPlateFragment fragment = new StarPlateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            id = getArguments().getString(Constant.Param.ID, "");
            name = getArguments().getString(Constant.Param.NAME, "");
        }
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();

        if (mAdapter != null) {
            mAdapter.addHeaderView(getHeaderView());
        }
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
        return mAdapter = new StarPlateAdapter(new StarPlateAdapter.OnItemListener() {
            @Override
            public void onFollow(View view, int position, PlateEntity item) {
                if (item.followed) {
                    cancelFollow(false, item, position);
                } else {
                    requestFollow(false, item, position);
                }
            }

            @Override
            public void onItem(View view, PlateEntity item) {

            }
        });
    }

    @Override
    protected Observable<Result<List<PlateEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.GET_STAR_PLATE_LIST)
                .params("boardId", id)
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

    @Override
    protected boolean loadOnShow() {
        return false;
    }

    private View getHeaderView() {
        View header = View.inflate(getActivity(), R.layout.forum_star_plate_header, null);
        RecyclerView recyclerView = header.findViewById(R.id.recycler_plate);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setAdapter(mHeaderAdapter = new StarPlateHeaderAdapter(new StarPlateHeaderAdapter.OnItemListener() {
            @Override
            public void onFollow(View view, int position, PlateEntity item) {
                if (item.followed) {
                    cancelFollow(true, item, position);
                } else {
                    requestFollow(true, item, position);
                }
            }

            @Override
            public void onItem(View view, PlateEntity item) {

            }
        }));

//        mPlateAdapter.setLoadMoreView(new HorizontalLoadMoreView());
//        mPlateAdapter.setOnLoadMoreListener(() -> {
//        }, recyclerView);

        EasyHttp.get(ApiUrl.GET_STAR_PLATE_HEADER)
                .params("boardId", id)
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
                            mHeaderAdapter.setNewData(list);
                        }
                    }
                });

        return header;
    }

    // 请求关注
    private void requestFollow(final boolean isHeader, final PlateEntity entity, final int position) {
        EasyHttp.post(ApiUrl.USER_REQUEST_FOLLOW)
                .upJson(entity.userId)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        entity.followed = true;
                        if (isHeader) {
                            mHeaderAdapter.notifyItemChanged(position);
                        } else {
                            mAdapter.notifyItemChanged(position);
                        }
                    }
                });
    }


    // 取消关注
    private void cancelFollow(final boolean isHeader, final PlateEntity entity, final int position) {
        EasyHttp.post(ApiUrl.USER_CANCEL_FOLLOW)
                .upJson(entity.userId)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        entity.followed = false;
                        if (isHeader) {
                            mHeaderAdapter.notifyItemChanged(position);
                        } else {
                            mAdapter.notifyItemChanged(position);
                        }
                    }
                });
    }
}
