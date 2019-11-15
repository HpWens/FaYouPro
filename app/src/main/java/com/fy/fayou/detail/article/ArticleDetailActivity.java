package com.fy.fayou.detail.article;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.detail.adapter.CommentHeaderPresenter;
import com.fy.fayou.detail.adapter.CommentPresenter;
import com.fy.fayou.detail.adapter.FooterPresenter;
import com.fy.fayou.detail.adapter.HeaderPresenter;
import com.fy.fayou.detail.adapter.PicPresenter;
import com.fy.fayou.detail.adapter.RecommendHeaderPresenter;
import com.fy.fayou.detail.adapter.RecommendPresenter;
import com.fy.fayou.detail.adapter.TextPresenter;
import com.fy.fayou.detail.bean.CommentBean;
import com.fy.fayou.detail.bean.CommentHeaderBean;
import com.fy.fayou.detail.bean.RecommendBean;
import com.fy.fayou.detail.bean.RecommendHeaderBean;
import com.fy.fayou.detail.dialog.BottomShareDialog;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.adapter.MeiBaseMixAdapter;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.Jzvd;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@Route(path = "/detail/article")
public class ArticleDetailActivity extends BaseActivity {

    @Autowired(name = "article_id")
    public String id;

    // 类型 0 文章 1 视频
    @Autowired(name = "type")
    public int type;

    @BindView(R.id.rl_article_navigation)
    RelativeLayout rlArticleNavigation;
    @BindView(R.id.rl_video_navigation)
    RelativeLayout rlVideoNavigation;
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
        ARouter.getInstance().inject(this);
        Eyes.translucentStatusBar(this, true, type == 1 ? false : true);
    }

    @Override
    protected void initData() {
        rlArticleNavigation.setVisibility(type != 1 ? View.VISIBLE : View.GONE);
        rlVideoNavigation.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        mAdapter = new MeiBaseMixAdapter();
        mAdapter.addItemPresenter(new CommentPresenter(new CommentPresenter.OnClickListener() {
            @Override
            public void onPraise(View v, int pos, CommentBean item) {
                requestPraise(item.id, pos, item);
            }

            @Override
            public void onLook(View view) {
                showBottomDialog();
            }
        }));
        mAdapter.addItemPresenter(new CommentHeaderPresenter());
        mAdapter.addItemPresenter(new FooterPresenter());
        mAdapter.addItemPresenter(new HeaderPresenter());
        mAdapter.addItemPresenter(new PicPresenter());
        mAdapter.addItemPresenter(new RecommendHeaderPresenter());
        mAdapter.addItemPresenter(new RecommendPresenter());
        mAdapter.addItemPresenter(new TextPresenter());
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(mAdapter);
        recycler.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                if (view.getTag() != null && view.getTag().toString().equals("video")) {
                    Jzvd jzvd = view.findViewById(R.id.video_player);
                    if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
                            jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                        if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                            Jzvd.releaseAllVideos();
                        }
                    }
                }
            }
        });

        requestData();
    }

    private void requestData() {
        Observable.zip(requestDetail(), requestComment(), (s, s2) -> {
            mDataList = new ArrayList<>();
            List<CommentBean> commentList = ParseUtils.parseListData(s2, "content", CommentBean.class);
            List<RecommendBean> recommendList = ParseUtils.parseListData(s, "recommendArticles", RecommendBean.class);

            if (commentList != null && !commentList.isEmpty()) {
                mDataList.add(new CommentHeaderBean());
                for (int i = 0; i < commentList.size(); i++) {
                    CommentBean bean = commentList.get(i);
                    bean.childIndex = i;
                    bean.lastIndex = (i == commentList.size() - 1);
                    mDataList.add(bean);
                }
            }

            if (recommendList != null && !recommendList.isEmpty()) {
                mDataList.add(new RecommendHeaderBean());
                for (int i = 0; i < recommendList.size(); i++) {
                    RecommendBean bean = recommendList.get(i);
                    bean.childIndex = i;
                    bean.lastIndex = (i == recommendList.size() - 1);
                    mDataList.add(bean);
                }
            }

            return mDataList;
        }).compose(bindToLifecycle())
                .subscribe(new Observer<List<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Object> objects) {
                        mAdapter.setNewData(objects);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // cao 后台 500
                        if (e instanceof ApiException) {
                            ParseUtils.handlerApiError((ApiException) e, error -> {
                                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                            });
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private Observable<String> requestDetail() {
        return EasyHttp.get(ApiUrl.ARTICLE_DETAIL + id)
                .execute(String.class);
    }

    private Observable<String> requestComment() {
        return EasyHttp.get(ApiUrl.COMMENT_LIST)
                .params("articleId", id)
                .params("parentId", "0")
                .params("page", "0")
                .params("size", "3")
                .execute(String.class);
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_detail_article;
    }

    @OnClick({R.id.tv_publish, R.id.iv_right_more, R.id.tv_message, R.id.iv_back_white,
            R.id.tv_collect, R.id.tv_share, R.id.iv_back, R.id.iv_more_white})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_white:
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_message:
            case R.id.tv_publish:
                showBottomDialog();
                break;
            case R.id.tv_collect:
                break;
            case R.id.tv_share:
                break;
            case R.id.iv_more_white:
            case R.id.iv_right_more:
                showDialog(new BottomShareDialog().setOnItemClickListener(() -> {
                    transMask.setVisibility(View.GONE);
                }));
                transMask.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showBottomDialog() {
        if (mReviewFragment == null) {
            loadRootFragment(R.id.fl_comment, mReviewFragment = ReviewFragment.newInstance(id));
            mReviewFragment.setOnReviewListener(new ReviewFragment.OnReviewListener() {
                @Override
                public void onDismiss() {
                    hideReviewFragment();
                }

                @Override
                public void onSlide(float ratio) {
                    transMask.setAlpha(ratio);
                }
            });
            recycler.postDelayed(() -> {
                showReviewFragment();
            }, 200);
        } else {
            showHideFragment(mReviewFragment);
            showReviewFragment();
        }
    }

    /**
     * 显示评论页
     */
    public void showReviewFragment() {
        if (mReviewFragment == null) return;
        mReviewFragment.showBehavior();
        transMask.post(() -> {
            transMask.setVisibility(View.VISIBLE);
        });
    }

    /**
     * 隐藏评论页
     */
    public void hideReviewFragment() {
        if (mReviewFragment == null) return;
        hideFragment(mReviewFragment);
        transMask.setVisibility(View.GONE);
    }

    /**
     * 请求点赞
     *
     * @param id
     * @param item
     */
    private void requestPraise(String id, int position, CommentBean item) {
        EasyHttp.post(ApiUrl.COMMENT_PRAISE + id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        if (item.give) {
                            item.gives -= 1;
                        } else {
                            item.gives += 1;
                        }
                        item.give = !item.give;
                        mAdapter.notifyItemChanged(position);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressedSupport() {
        if (mReviewFragment != null && mReviewFragment.isShowing()) {
            mReviewFragment.hideBehavior();
            return;
        }
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressedSupport();
    }
}
