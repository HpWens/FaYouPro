package com.fy.fayou.detail.article;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.detail.adapter.SecondReviewAdapter;
import com.fy.fayou.detail.bean.CommentBean;
import com.fy.fayou.detail.dialog.BottomCommentDialog;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;

public class SecondReviewFragment extends BaseMultiListFragment<CommentBean> {

    private String id;
    private CommentBean parent;

    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.comment_layout)
    LinearLayout commentLayout;
    @BindView(R.id.behavior_layout)
    RelativeLayout behaviorLayout;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;

    Unbinder unbinder;
    private BottomSheetBehavior mBehavior;
    private SecondReviewAdapter mAdapter;

    public static SecondReviewFragment newInstance(String id, CommentBean bean) {
        Bundle args = new Bundle();
        args.putString(Constant.Param.ID, id);
        args.putSerializable(Constant.Param.BEAN, bean);
        SecondReviewFragment fragment = new SecondReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, getView());
        if (getArguments() != null) {
            id = getArguments().getString(Constant.Param.ID, "0");
            parent = (CommentBean) getArguments().getSerializable(Constant.Param.BEAN);
        }
        super.initView();

        mBehavior = BottomSheetBehavior.from(behaviorLayout);
        mBehavior.setPeekHeight(0);
        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    hideFragment(SecondReviewFragment.this);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });
    }

    public void showBehavior() {
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void hideBehavior() {
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public boolean isShowing() {
        return mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
    }

    @Override
    protected void initData() {
        super.initData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected RecyclerView getRecyclerView() {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recycler;
    }

    @Override
    protected BaseMultiAdapter<CommentBean> getAdapter() {
        return mAdapter = new SecondReviewAdapter(new ArrayList<>(), new SecondReviewAdapter.OnClickListener() {
            @Override
            public void onPraise(View v, int position, CommentBean comment) {
                if (UserService.getInstance().checkLoginAndJump()) {
                    requestPraise(comment.id, position, comment);
                }
            }

            @Override
            public void onComment(String userName, String articleId, String parentId, int position, String reUserId) {
                showBottomCommentDialog(userName, articleId, parentId, position, reUserId);
            }

            @Override
            public void onJumpThreeComment(String parentId) {

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
    protected int getLayoutId() {
        return R.layout.fragment_second_review;
    }

    @OnClick({R.id.iv_back, R.id.tv_publish, R.id.tv_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                hideBehavior();
                break;
            case R.id.tv_publish:
                break;
            case R.id.tv_send:
                break;
        }
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

    /**
     * 底部提示框
     *
     * @param userName
     * @param articleId
     * @param parentId
     */
    private void showBottomCommentDialog(String userName, String articleId, String parentId, int position, String reUserId) {
        showDialog(new BottomCommentDialog().setParams(userName, articleId, parentId, position)
                .setForum(true)
                .setReUserId(reUserId)
                .setOnPublishListener((isParent, pos, entity) -> {
                    // 更新列表评论
                    updateData(entity);
                }));
    }

    // 更新数据
    public void updateData(CommentBean entity) {
        if (mAdapter == null) return;
        if (mAdapter.getData().isEmpty()) setState(ViewState.COMPLETED);
        int pos = mAdapter.getData().size() > 0 ? 1 : 0;
        mAdapter.getData().add(pos, entity);
        mAdapter.notifyItemInserted(pos);
        recycler.smoothScrollToPosition(pos);
    }

    @Override
    public boolean onBackPressedSupport() {
        if (isShowing()) {
            hideBehavior();
            return true;
        }
        return super.onBackPressedSupport();
    }
}
