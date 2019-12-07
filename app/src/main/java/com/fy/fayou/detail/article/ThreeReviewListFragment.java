package com.fy.fayou.detail.article;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.detail.adapter.ThreeReviewAdapter;
import com.fy.fayou.detail.bean.CommentBean;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.status.ViewState;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

import io.reactivex.Observable;

public class ThreeReviewListFragment extends BaseListFragment<CommentBean> {

    private String parentId;
    private String reUserId;
    private String userId;

    private RecyclerView mRecyclerView;
    private ThreeReviewAdapter mAdapter;
    private OnItemClickListener mListener;

    public static ThreeReviewListFragment newInstance(String parentId, String reUserId, String userId) {
        Bundle args = new Bundle();
        args.putString(Constant.Param.ID, parentId);
        args.putString(Constant.Param.USER_ID, userId);
        args.putString(Constant.Param.RE_USER_ID, reUserId);
        ThreeReviewListFragment fragment = new ThreeReviewListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            parentId = getArguments().getString(Constant.Param.ID, "0");
            reUserId = getArguments().getString(Constant.Param.RE_USER_ID, "0");
            userId = getArguments().getString(Constant.Param.USER_ID, "0");
        }
        super.initView();
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = getView().findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRecyclerView;
    }

    @Override
    public ThreeReviewAdapter getAdapter() {
        return mAdapter = new ThreeReviewAdapter(getActivity(), new ThreeReviewAdapter.OnClickListener() {
            @Override
            public void onPraise(View v, int position, CommentBean comment) {
                if (UserService.getInstance().checkLoginAndJump()) {
                    requestPraise(comment.id, position, comment);
                }
            }

            @Override
            public void onComment(String userName, String articleId, int position, String reUserId) {
                if (mListener != null) {
                    mListener.onClick(userName, articleId, parentId, position, reUserId);
                }
            }
        });
    }

    @Override
    protected Observable<Result<List<CommentBean>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.GET_FORUM_THREE_COMMENT)
                .params("parentId", parentId)
                .params("reUserId", reUserId)
                .params("userId", userId)
                .params("page", (pageNo - 1) + "")
                .params("size", "20")
                .execute(String.class);
        return getListByField(observable, "content", CommentBean.class);
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
    protected boolean loadOnShow() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_recycler;
    }

    /**
     * 请求点赞
     *
     * @param id
     * @param item
     */
    private void requestPraise(String id, int position, CommentBean item) {
        EasyHttp.post(ApiUrl.FORUM_COMMENT_PRAISE + id + "/give")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        ParseUtils.handlerApiError(e, error -> {
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (item.given) {
                            item.gives -= 1;
                        } else {
                            item.gives += 1;
                        }
                        item.given = !item.given;
                        mAdapter.notifyItemChanged(position);
                    }
                });
    }

    // 更新数据
    public void updateData(CommentBean entity) {
        if (mAdapter == null) return;
        if (mAdapter.getData().isEmpty()) setState(ViewState.COMPLETED);
        mAdapter.getData().add(0, entity);
        mAdapter.notifyItemInserted(0);
        mRecyclerView.smoothScrollToPosition(0);
    }

    public interface OnItemClickListener {
        void onClick(String userName, String articleId, String parentId, int position, String reUserId);
    }

    public ThreeReviewListFragment setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
        return this;
    }
}
