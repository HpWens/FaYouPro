package com.fy.fayou.detail.article;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fy.fayou.R;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.detail.bean.CommentBean;
import com.fy.fayou.detail.dialog.BottomCommentDialog;
import com.meis.base.mei.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SecondReviewFragment extends BaseFragment {

    private String id;
    private CommentBean parent;

    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.iv_back)
    ImageView ivBack;
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
    private ThreeReviewFragment mThreeReviewFragment;
    private SecondReviewListFragment mSecondReviewListFragment;

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
        loadRootFragment(R.id.fl_container, mSecondReviewListFragment = SecondReviewListFragment.newInstance(id, parent)
                .setOnItemClickListener(new SecondReviewListFragment.OnItemClickListener() {
                    @Override
                    public void onClick(String userName, String articleId, String parentId, int position, String reUserId) {
                        showBottomCommentDialog(userName, articleId, parentId, position, reUserId);
                    }

                    @Override
                    public void onJumpThreeReview(String userId, String reUserId) {
                        showThreeReviewDialog(parent.id, userId, reUserId);
                    }
                }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 初始化三级评论
     *
     * @param parentId
     */
    public void showThreeReviewDialog(String parentId, String userId, String reUserId) {
        loadRootFragment(R.id.fl_three_comment, mThreeReviewFragment = ThreeReviewFragment.newInstance(parentId, reUserId, userId));
        ivBack.postDelayed(() -> {
            showThreeReviewFragment();
        }, 200);
    }

    /**
     * 显示三级评论
     */
    public void showThreeReviewFragment() {
        if (mThreeReviewFragment == null) return;
        mThreeReviewFragment.showBehavior();
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
            case R.id.tv_send:
                if (UserService.getInstance().checkLoginAndJump()) {
                    showBottomCommentDialog("", parent.postId, id, 0, parent.userId);
                }
                break;
        }
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
                    if (mSecondReviewListFragment != null) {
                        mSecondReviewListFragment.updateData(entity);
                    }
                }));
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
