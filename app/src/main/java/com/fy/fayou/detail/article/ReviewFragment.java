package com.fy.fayou.detail.article;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fy.fayou.R;
import com.fy.fayou.detail.dialog.BottomCommentDialog;
import com.meis.base.mei.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReviewFragment extends BaseFragment {

    @BindView(R.id.tv_total)
    TextView tvTotal;

    @BindView(R.id.behavior_layout)
    RelativeLayout behaviorLayout;

    @BindView(R.id.tv_publish)
    TextView tvPublish;

    @BindView(R.id.tv_send)
    TextView tvSend;

    Unbinder unbinder;

    private BottomSheetBehavior mBehavior;
    private OnReviewListener mListener;

    public static ReviewFragment newInstance() {
        Bundle args = new Bundle();
        ReviewFragment fragment = new ReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, getView());

        mBehavior = BottomSheetBehavior.from(behaviorLayout);
        mBehavior.setPeekHeight(0);
        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    if (mListener != null) {
                        mListener.onDismiss();
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                if (mListener != null) {
                    mListener.onSlide(v);
                }
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
        loadRootFragment(R.id.fl_recycler, ReviewListFragment.newInstance());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_review;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public ReviewFragment setOnReviewListener(OnReviewListener listener) {
        mListener = listener;
        return this;
    }

    @OnClick({R.id.comment_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment_layout:
                showDialog(new BottomCommentDialog());
                break;
        }
    }

    public interface OnReviewListener {
        void onDismiss();

        void onSlide(float ratio);
    }
}
