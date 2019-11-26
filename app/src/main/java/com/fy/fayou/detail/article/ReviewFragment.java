package com.fy.fayou.detail.article;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.UserService;
import com.fy.fayou.detail.dialog.BottomCommentDialog;
import com.meis.base.mei.base.BaseFragment;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

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

    // 文章id
    private String articleId;
    // 父评论id
    private String parentId;

    private int totalComment = 0;

    public static final String ARTICLE_ID = "article_id";
    public static final String PARENT_ID = "parent_id";

    Unbinder unbinder;

    private BottomSheetBehavior mBehavior;
    private OnReviewListener mListener;
    private ReviewListFragment mReviewListFragment;

    public static ReviewFragment newInstance(String articleId) {
        Bundle args = new Bundle();
        args.putString(ARTICLE_ID, articleId);
        ReviewFragment fragment = new ReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            articleId = getArguments().getString(ARTICLE_ID, "");
        }
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
        if (mReviewListFragment != null) mReviewListFragment.scrollTop();
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
        // 请求评论总数量
        requestCount();

        loadRootFragment(R.id.fl_recycler, mReviewListFragment = ReviewListFragment.newInstance(articleId)
                .setOnItemClickListener((userName, articleId, parentId, position) -> {
                    if (UserService.getInstance().checkLoginAndJump()) {
                        showBottomCommentDialog(userName, articleId, parentId, position);
                    }
                }));
    }

    private void requestCount() {
        EasyHttp.get(ApiUrl.COMMENT_COUNT)
                .params("articleId", articleId)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            try {
                                JSONObject json = new JSONObject(s);
                                if (json.has("count")) {
                                    tvTotal.setText(getResources().getString(R.string.comment_count, totalComment = json.optInt("count")));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
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

    @OnClick({R.id.comment_layout, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment_layout:
                if (UserService.getInstance().checkLoginAndJump()) {
                    showBottomCommentDialog("", articleId, "", 0);
                }
                break;
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
    private void showBottomCommentDialog(String userName, String articleId, String parentId, int position) {
        showDialog(new BottomCommentDialog().setParams(userName, articleId, parentId, position).setOnPublishListener((isParent, pos, entity) -> {
            // 更新列表评论
            if (mReviewListFragment != null) {
                mReviewListFragment.updateData(isParent, pos, entity);
            }
            totalComment += 1;
            tvTotal.setText(getResources().getString(R.string.comment_count, totalComment));
        }));
    }

    public interface OnReviewListener {
        void onDismiss();

        void onSlide(float ratio);
    }
}
