package com.fy.fayou.detail.article;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fy.fayou.R;
import com.fy.fayou.common.Constant;
import com.fy.fayou.detail.dialog.BottomCommentDialog;
import com.meis.base.mei.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ThreeReviewFragment extends BaseFragment {

    private String parentId;
    private String reUserId;
    private String userId;

    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.behavior_layout)
    RelativeLayout behaviorLayout;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;

    private Unbinder unbinder;
    private BottomSheetBehavior mBehavior;
    private ThreeReviewListFragment mThreeReviewListFragment;

    public static ThreeReviewFragment newInstance(String parentId, String reUserId, String userId) {
        Bundle args = new Bundle();
        args.putString(Constant.Param.ID, parentId);
        args.putString(Constant.Param.USER_ID, userId);
        args.putString(Constant.Param.RE_USER_ID, reUserId);
        ThreeReviewFragment fragment = new ThreeReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, getView());
        if (getArguments() != null) {
            parentId = getArguments().getString(Constant.Param.ID, "0");
            reUserId = getArguments().getString(Constant.Param.RE_USER_ID, "0");
            userId = getArguments().getString(Constant.Param.USER_ID, "0");
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
                    hideFragment(ThreeReviewFragment.this);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });
    }

    @Override
    protected void initData() {
        loadRootFragment(R.id.fl_container, mThreeReviewListFragment = ThreeReviewListFragment.newInstance(parentId, reUserId, userId)
                .setOnItemClickListener((userName, articleId, parentId, position, reUserId) -> {
                    showBottomCommentDialog(userName, articleId, parentId, position, reUserId);
                }));
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_three_review;
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                hideBehavior();
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
                    if (mThreeReviewListFragment != null) {
                        mThreeReviewListFragment.updateData(entity);
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
