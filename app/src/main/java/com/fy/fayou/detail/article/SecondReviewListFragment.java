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
import com.fy.fayou.detail.adapter.SecondReviewAdapter;
import com.fy.fayou.detail.bean.CommentBean;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.adapter.BaseMultiAdapter;
import com.meis.base.mei.base.BaseMultiListFragment;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.status.ViewState;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class SecondReviewListFragment extends BaseMultiListFragment<CommentBean> {

    private String id;
    private CommentBean parent;

    private SecondReviewAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private OnItemClickListener mListener;

    public static SecondReviewListFragment newInstance(String id, CommentBean bean) {
        Bundle args = new Bundle();
        args.putString(Constant.Param.ID, id);
        args.putSerializable(Constant.Param.BEAN, bean);
        SecondReviewListFragment fragment = new SecondReviewListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            id = getArguments().getString(Constant.Param.ID, "0");
            parent = (CommentBean) getArguments().getSerializable(Constant.Param.BEAN);
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
    protected BaseMultiAdapter<CommentBean> getAdapter() {
        return mAdapter = new SecondReviewAdapter(getActivity(), new ArrayList<>(), new SecondReviewAdapter.OnClickListener() {
            @Override
            public void onPraise(View v, int position, CommentBean comment) {
                if (UserService.getInstance().checkLoginAndJump()) {
                    requestPraise(comment.id, position, comment);
                }
            }

            @Override
            public void onComment(String userName, int position, String reUserId) {
                if (mListener != null) {
                    mListener.onClick(userName, parent.postId, parent.id, position, reUserId);
                }
            }

            @Override
            public void onJumpThreeComment(String userId, String reUserId) {
                if (mListener != null) {
                    mListener.onJumpThreeReview(userId, reUserId);
                }
            }
        });
    }

    @Override
    protected Observable<Result<List<CommentBean>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.GET_FORUM_SECOND_COMMENT)
                .params("parentId", id)
                .params("page", (pageNo - 1) + "")
                .params("size", "20")
                .execute(String.class);
        return getListByField(observable, "content", CommentBean.class);
    }

    @Override
    protected void onDataLoaded(int pageNo, Result<List<CommentBean>> result) {
        super.onDataLoaded(pageNo, result);
        if (pageNo == 1) {
            parent.level = 1;
            mAdapter.addData(0, parent);
        }
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
        int pos = mAdapter.getData().size() > 0 ? 1 : 0;
        mAdapter.getData().add(pos, entity);
        mAdapter.notifyItemInserted(pos);
        mRecyclerView.smoothScrollToPosition(pos);
    }

    public interface OnItemClickListener {
        void onClick(String userName, String articleId, String parentId, int position, String reUserId);

        void onJumpThreeReview(String userId, String reUserId);
    }

    public SecondReviewListFragment setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
        return this;
    }
}
