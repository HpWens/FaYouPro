package com.fy.fayou.detail.article;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.fy.fayou.R;
import com.fy.fayou.bean.UserInfo;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.detail.adapter.CommentHeaderPresenter;
import com.fy.fayou.detail.adapter.CommentPresenter;
import com.fy.fayou.detail.adapter.FooterPresenter;
import com.fy.fayou.detail.adapter.HeaderPresenter;
import com.fy.fayou.detail.adapter.KtEmptyCommentPresenter;
import com.fy.fayou.detail.adapter.PicPresenter;
import com.fy.fayou.detail.adapter.RecommendHeaderPresenter;
import com.fy.fayou.detail.adapter.RecommendPresenter;
import com.fy.fayou.detail.adapter.TextPresenter;
import com.fy.fayou.detail.bean.ArticleEntity;
import com.fy.fayou.detail.bean.CommentBean;
import com.fy.fayou.detail.bean.CommentHeaderBean;
import com.fy.fayou.detail.bean.EmptyCommentBean;
import com.fy.fayou.detail.bean.FooterBean;
import com.fy.fayou.detail.bean.HeaderBean;
import com.fy.fayou.detail.bean.PicBean;
import com.fy.fayou.detail.bean.RecommendBean;
import com.fy.fayou.detail.bean.RecommendHeaderBean;
import com.fy.fayou.detail.bean.TextBean;
import com.fy.fayou.detail.dialog.BottomShareDialog;
import com.fy.fayou.event.LoginSuccessOrExitEvent;
import com.fy.fayou.event.ReportSuccessEvent;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.MarkDownParser;
import com.fy.fayou.utils.ParseUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.meis.base.mei.adapter.MeiBaseMixAdapter;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
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
    ImageView ivCollect;
    @BindView(R.id.tv_share)
    ImageView tvShare;
    @BindView(R.id.line)
    View transMask;
    @BindView(R.id.jzvdstd_player)
    JzvdStd jzvdStd;

    private boolean isCollect = false;
    private String articleId;

    MeiBaseMixAdapter mAdapter;
    List<Object> mDataList = new ArrayList<>();

    private BottomShareDialog mShareDialog;
    private ReviewFragment mReviewFragment;
    private List<LocalMedia> mPicMedia = new ArrayList<>();
    private int mPicIndex = 0;

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
                if (UserService.getInstance().checkLoginAndJump()) {
                    requestPraise(item.id, pos, item);
                }
            }

            @Override
            public void onLook(View view) {
                showBottomDialog();
            }
        }));
        mAdapter.addItemPresenter(new CommentHeaderPresenter());
        mAdapter.addItemPresenter(new FooterPresenter((v, id, pos, footer) -> {
            if (UserService.getInstance().checkLoginAndJump()) {
                requestPraise(footer, pos);
            }
        }));
        mAdapter.addItemPresenter(new HeaderPresenter((v, id, position, header) -> {
            if (UserService.getInstance().checkLoginAndJump()) {
                requestFollow(header, position);
            }
        }));
        mAdapter.addItemPresenter(new PicPresenter((v, item) -> {
            PictureSelector.create(this).themeStyle(R.style.picture_default_style).openExternalPreview(item.position, mPicMedia);
        }));
        mAdapter.addItemPresenter(new RecommendHeaderPresenter());
        mAdapter.addItemPresenter(new RecommendPresenter((v, item) -> {
            ARoute.jumpDetail(item.id, item.articleType);

            // 新增浏览记录
            HashMap<String, String> params = new HashMap<>();
            params.put("businessId", item.id);
            params.put("browseRecordType", item.articleType);
            JSONObject jsonObject = new JSONObject(params);
            EasyHttp.post(ApiUrl.MY_HISTORY)
                    .upJson(jsonObject.toString())
                    .execute(new SimpleCallBack<String>() {
                        @Override
                        public void onError(ApiException e) {
                        }

                        @Override
                        public void onSuccess(String s) {
                        }
                    });
        }));
        mAdapter.addItemPresenter(new TextPresenter());
        mAdapter.addItemPresenter(new KtEmptyCommentPresenter());
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
            if (TextUtils.isEmpty(s) || TextUtils.isEmpty(s2)) return mDataList;
            List<CommentBean> commentList = ParseUtils.parseListData(s2, "content", CommentBean.class);
            ArticleEntity articleEntity = ParseUtils.parseData(s, ArticleEntity.class);
            List<RecommendBean> recommendList = articleEntity.recommendArticles;

            // 填充视频
            if (articleEntity.articleType.equals(Constant.Param.VIDEO)) {
                jzvdStd.setUp(getNonEmpty(articleEntity.videoUrl), "");
                jzvdStd.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(mContext)
                        .load(getNonEmpty(articleEntity.cover))
                        .apply(GlideOption.getFullScreenWOption(mContext))
                        .into(jzvdStd.thumbImageView);
            }

            fillHeaderData(articleEntity);

            fillContentData(articleEntity);

            fillFooterData(articleEntity);

            if (commentList != null && !commentList.isEmpty()) {
                mDataList.add(new CommentHeaderBean());
                for (int i = 0; i < commentList.size(); i++) {
                    CommentBean bean = commentList.get(i);
                    bean.childIndex = i;
                    bean.lastIndex = (i == commentList.size() - 1);
                    mDataList.add(bean);
                }
            } else {
                mDataList.add(new CommentHeaderBean());
                mDataList.add(new EmptyCommentBean());
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

            // 请求是否收藏接口
            articleId = articleEntity.id;
            requestCollectData(articleEntity.id);

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

    // 添加尾部数据
    private void fillFooterData(ArticleEntity articleEntity) {
        FooterBean footerBean = new FooterBean();
        footerBean.author = articleEntity.author;
        footerBean.source = articleEntity.source;
        footerBean.tagNames = articleEntity.tagNames;
        footerBean.gives = articleEntity.gives;
        footerBean.give = articleEntity.give;
        footerBean.id = articleEntity.id;
        footerBean.giveRecords = articleEntity.giveRecords;
        mDataList.add(footerBean);
    }

    // 填充内容
    private void fillContentData(ArticleEntity articleEntity) {
        String content = articleEntity.content;
        mPicMedia = new ArrayList<>();
        mPicIndex = 0;
        if (!TextUtils.isEmpty(content)) {
            new MarkDownParser().readMarkdownByJson(content, new MarkDownParser.OnMarkListener() {
                @Override
                public void onPic(String pic) {
                    PicBean picBean = new PicBean();
                    picBean.httpPath = pic;
                    picBean.position = mPicIndex;
                    mDataList.add(picBean);

                    LocalMedia media = new LocalMedia();
                    media.setPath(pic);
                    mPicMedia.add(media);

                    mPicIndex++;
                }

                @Override
                public void onText(String text) {
                    TextBean textBean = new TextBean();
                    textBean.text = text;
                    mDataList.add(textBean);
                }
            });
        }
    }

    // 填充头部数据
    private void fillHeaderData(ArticleEntity articleEntity) {
        HeaderBean header = new HeaderBean();
        header.fullTitle = articleEntity.fullTitle;
        header.auditName = articleEntity.auditName;
        header.createTime = articleEntity.createTime;
        header.follow = articleEntity.follow;
        header.auditId = articleEntity.auditId;
        header.auditAvatar = articleEntity.auditAvatar;
        mDataList.add(header);
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

    private void requestPraise(FooterBean footer, int position) {
        EasyHttp.post(ApiUrl.ARTICLE_PRAISE + footer.id + "/give")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (footer.give) {
                            footer.gives -= 1;
                            for (ArticleEntity.UserBean userBean : footer.giveRecords) {
                                if (userBean.userId.equals(UserService.getInstance().getUserId())) {
                                    footer.giveRecords.remove(userBean);
                                    break;
                                }
                            }
                        } else {
                            boolean exist = false;
                            for (ArticleEntity.UserBean userBean : footer.giveRecords) {
                                if (userBean.userId.equals(UserService.getInstance().getUserId())) {
                                    exist = true;
                                    break;
                                }
                            }
                            if (!exist) {
                                UserInfo userInfo = UserService.getInstance().getUserInfo();
                                ArticleEntity.UserBean userBean = new ArticleEntity.UserBean();
                                userBean.userId = userInfo.id;
                                userBean.userAvatar = userInfo.avatar;
                                footer.giveRecords.add(userBean);
                            }
                            footer.gives += 1;
                        }
                        footer.give = !footer.give;
                        mAdapter.notifyItemChanged(position);
                    }
                });
    }

    private void requestFollow(HeaderBean header, int position) {
        EasyHttp.post(header.follow ? ApiUrl.UN_FOLLOW_USER : ApiUrl.FOLLOW_USER)
                .upJson(header.auditId)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        header.follow = !header.follow;
                        mAdapter.notifyItemChanged(position);
                    }
                });
    }

    /**
     * @param articleId
     */
    private void requestCollectData(String articleId) {
        EasyHttp.get(ApiUrl.IS_COLLECT)
                .params("businessId", "" + articleId)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        isCollect = ParseUtils.getFieldByJson(s, "exist");
                        ivCollect.setSelected(isCollect);
                    }
                });
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
                if (UserService.getInstance().checkLoginAndJump()) {
                    requestCollect(articleId);
                }
                break;
            case R.id.tv_share:
                break;
            case R.id.iv_more_white:
            case R.id.iv_right_more:
                showDialog(mShareDialog = new BottomShareDialog().setCollect(isCollect).setArticleId(articleId)
                        .setOnItemClickListener(new BottomShareDialog.OnItemClickListener() {
                            @Override
                            public void onDismiss() {
                                transMask.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCollect(boolean collected) {
                                isCollect = collected;
                                ivCollect.setSelected(isCollect);
                            }
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

    private void requestCollect(String articleId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("businessId", articleId);
        params.put("collectType", "ARTICLE");
        JSONObject jsonObject = new JSONObject(params);

        EasyHttp.post(ApiUrl.MY_COLLECT)
                .upJson(jsonObject.toString())
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        isCollect = !isCollect;
                        ivCollect.setSelected(isCollect);
                    }
                });
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

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccessEvent(LoginSuccessOrExitEvent event) {
        if (recycler != null) requestData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReportSuccessEvent(ReportSuccessEvent event) {
        if (mShareDialog != null && mShareDialog.isAdded()) {
            mShareDialog.dismiss();
        }
    }
}
