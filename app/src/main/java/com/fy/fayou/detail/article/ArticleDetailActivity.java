package com.fy.fayou.detail.article;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.fy.fayou.detail.adapter.CommentPresenter;
import com.fy.fayou.detail.adapter.FooterPresenter;
import com.fy.fayou.detail.adapter.HeaderPresenter;
import com.fy.fayou.detail.adapter.PicPresenter;
import com.fy.fayou.detail.adapter.RecommendHeaderPresenter;
import com.fy.fayou.detail.adapter.RecommendPresenter;
import com.fy.fayou.detail.adapter.TextPresenter;
import com.fy.fayou.detail.bean.CommentBean;
import com.fy.fayou.detail.bean.FooterBean;
import com.fy.fayou.detail.bean.HeaderBean;
import com.fy.fayou.detail.bean.PicBean;
import com.fy.fayou.detail.bean.RecommendBean;
import com.fy.fayou.detail.bean.RecommendHeaderBean;
import com.fy.fayou.detail.bean.TextBean;
import com.meis.base.mei.adapter.MeiBaseMixAdapter;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/detail/article")
public class ArticleDetailActivity extends BaseActivity {


    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.tv_message)
    ImageView tvMessage;
    @BindView(R.id.tv_collect)
    ImageView tvCollect;
    @BindView(R.id.tv_share)
    ImageView tvShare;
    @BindView(R.id.line)
    View transMask;

    MeiBaseMixAdapter mAdapter;
    List<Object> mDataList = new ArrayList<>();

    private ReviewFragment mReviewFragment;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.translucentStatusBar(this, true, true);
    }

    @Override
    protected void initData() {
        mAdapter = new MeiBaseMixAdapter();
        mAdapter.addItemPresenter(new CommentPresenter());
        mAdapter.addItemPresenter(new FooterPresenter());
        mAdapter.addItemPresenter(new HeaderPresenter());
        mAdapter.addItemPresenter(new PicPresenter());
        mAdapter.addItemPresenter(new RecommendHeaderPresenter());
        mAdapter.addItemPresenter(new RecommendPresenter());
        mAdapter.addItemPresenter(new TextPresenter());
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(mAdapter);


        HeaderBean headerBean = new HeaderBean();
        mDataList.add(headerBean);

        PicBean picBean = new PicBean();
        mDataList.add(picBean);

        TextBean textBean = new TextBean();
        mDataList.add(textBean);

        picBean = new PicBean();
        mDataList.add(picBean);

        textBean = new TextBean();
        mDataList.add(textBean);

        FooterBean footerBean = new FooterBean();
        mDataList.add(footerBean);

        RecommendHeaderBean recommendHeaderBean = new RecommendHeaderBean();
        mDataList.add(recommendHeaderBean);

        RecommendBean recommendBean = new RecommendBean();
        mDataList.add(recommendBean);
        recommendBean = new RecommendBean();
        mDataList.add(recommendBean);
        recommendBean = new RecommendBean();
        mDataList.add(recommendBean);

        CommentBean commentBean = new CommentBean();
        mDataList.add(commentBean);
        commentBean = new CommentBean();
        mDataList.add(commentBean);
        commentBean = new CommentBean();
        mDataList.add(commentBean);
        commentBean = new CommentBean();
        mDataList.add(commentBean);

        mAdapter.setNewData(mDataList);
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_detail_article;
    }

    @OnClick({R.id.tv_publish, R.id.tv_message, R.id.tv_collect, R.id.tv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_publish:
                showBottomDialog();
                break;
            case R.id.tv_message:
                break;
            case R.id.tv_collect:
                break;
            case R.id.tv_share:
                break;
        }
    }

    private void showBottomDialog() {
        if (mReviewFragment == null) {
            loadRootFragment(R.id.fl_comment, mReviewFragment = ReviewFragment.newInstance());
            mReviewFragment.setOnReviewListener(new ReviewFragment.OnReviewListener() {
                @Override
                public void onDismiss() {
                    hideFragment(mReviewFragment);
                    transMask.setVisibility(View.GONE);
                }

                @Override
                public void onSlide(float ratio) {
                    transMask.setAlpha(ratio);
                }
            });
            recycler.postDelayed(() -> {
                transMask.setVisibility(View.VISIBLE);
                mReviewFragment.showBehavior();
            }, 200);
        } else {
            transMask.setVisibility(View.VISIBLE);
            showHideFragment(mReviewFragment);
            mReviewFragment.showBehavior();
        }
    }
}
