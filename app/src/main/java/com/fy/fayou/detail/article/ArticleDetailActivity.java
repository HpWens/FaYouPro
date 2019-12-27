package com.fy.fayou.detail.article;

import android.content.res.Configuration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.fy.fayou.event.ListPraiseEvent;
import com.fy.fayou.event.LoginSuccessOrExitEvent;
import com.fy.fayou.event.RefreshDetailCommentEvent;
import com.fy.fayou.event.RefreshDetailPraiseEvent;
import com.fy.fayou.event.ReportSuccessEvent;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.MarkDownParser;
import com.fy.fayou.utils.ParseUtils;
import com.fy.fayou.view.LandLayoutVideo;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.meis.base.mei.adapter.MeiBaseMixAdapter;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

@Route(path = "/detail/article")
public class ArticleDetailActivity extends BaseActivity {

    @Autowired(name = "article_id")
    public String id;

    // 类型 0 文章 1 视频
    @Autowired(name = "type")
    public int type;

    // 记录列表位置
    @Autowired(name = "position")
    public int list_position;

    @BindView(R.id.rl_article_navigation)
    RelativeLayout rlArticleNavigation;
    @BindView(R.id.rl_video_navigation)
    RelativeLayout rlVideoNavigation;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.loading_layout)
    LinearLayout loadingLayout;
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
    @BindView(R.id.detail_player)
    LandLayoutVideo detailPlayer;

    private boolean isCollect = false;
    private String articleId;

    private LinearLayoutManager linearLayoutManager;
    MeiBaseMixAdapter mAdapter;
    List<Object> mDataList = new ArrayList<>();

    private BottomShareDialog mShareDialog;
    private String mShareUrl;
    private String mShareContent;
    private String mPublishName;
    private ReviewFragment mReviewFragment;
    private List<LocalMedia> mPicMedia = new ArrayList<>();
    private int mPicIndex = 0;

    // 视频相关
    private OrientationUtils orientationUtils;
    private boolean isPlay;
    private boolean isPause;

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
        }, type == Constant.Param.FORUM_TYPE));
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
            requestAddHistory(item.id, item.articleType);

            finish();
        }));
        mAdapter.addItemPresenter(new TextPresenter());
        mAdapter.addItemPresenter(new KtEmptyCommentPresenter());
        recycler.setLayoutManager(linearLayoutManager = new LinearLayoutManager(this));
        recycler.setAdapter(mAdapter);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals("video")
                            && (position < firstVisibleItem || position > lastVisibleItem)) {

                        //如果滑出去了上面和下面就是否，和今日头条一样
                        //是否全屏
                        if (!GSYVideoManager.isFullState(mContext)) {
                            GSYVideoManager.releaseAllVideos();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

        if (type == Constant.Param.FORUM_TYPE) {
            // 论坛详情页
            requestForumData();

            requestAddHistory(id, "FORUM");
        } else {
            // 文章视频详情页
            requestData();
        }
    }

    private void requestAddHistory(String id, String type) {
        // 新增浏览记录
        HashMap<String, String> params = new HashMap<>();
        params.put("businessId", id);
        params.put("browseRecordType", type);
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
    }

    // 请求论坛详情页数据
    private void requestForumData() {
        EasyHttp.get(ApiUrl.GET_FORUM_DETAIL + id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        handlerApiError(e);
                        loadingLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(String s) {
                        loadingLayout.setVisibility(View.GONE);
                        if (!TextUtils.isEmpty(s)) {
                            // 初始化数据源
                            mDataList = new ArrayList<>();
                            ArticleEntity articleEntity = ParseUtils.parseData(s, ArticleEntity.class);
                            // 填充帖子数据
                            fillingData(articleEntity, articleEntity.commentList, null);
                            mAdapter.setNewData(mDataList);
                        } else {
                            Toast.makeText(mContext, "该帖子已被删除", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void resolveNormalVideoUI() {
        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.GONE);
        detailPlayer.getBackButton().setVisibility(View.GONE);
    }

    /**
     * @param articleEntity
     * @param commentList
     * @param recommendList
     */
    private void fillingData(ArticleEntity articleEntity, List<CommentBean> commentList, List<RecommendBean> recommendList) {
        // 分享url
        mShareUrl = articleEntity.shareUrl;

        // 填充视频
        if (articleEntity.articleType != null && articleEntity.articleType.equals(Constant.Param.VIDEO)) {

            //增加封面
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(mContext)
                    .load(getNonEmpty(articleEntity.cover))
                    .apply(GlideOption.getFullScreenWOption(mContext))
                    .into(imageView);

            resolveNormalVideoUI();

            //外部辅助的旋转，帮助全屏
            orientationUtils = new OrientationUtils(this, detailPlayer);
            //初始化不打开外部的旋转
            orientationUtils.setEnable(false);

            GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
            gsyVideoOption.setThumbImageView(imageView)
                    .setIsTouchWiget(true)
                    .setRotateViewAuto(false)
                    .setLockLand(false)
                    .setAutoFullWithSize(true)
                    .setShowFullAnimation(false)
                    .setNeedLockFull(true)
                    .setUrl(getNonEmpty(articleEntity.videoUrl))
                    .setCacheWithPlay(false)
                    .setVideoTitle(getNonEmpty(articleEntity.fullTitle))
                    .setVideoAllCallBack(new GSYSampleCallBack() {
                        @Override
                        public void onPrepared(String url, Object... objects) {
                            super.onPrepared(url, objects);
                            //开始播放了才能旋转和全屏
                            orientationUtils.setEnable(true);
                            isPlay = true;

                            //设置 seek 的临近帧。
                            if (detailPlayer.getGSYVideoManager().getPlayer() instanceof Exo2PlayerManager) {
                            }
                        }

                        @Override
                        public void onQuitFullscreen(String url, Object... objects) {
                            super.onQuitFullscreen(url, objects);
                            if (orientationUtils != null) {
                                orientationUtils.backToProtVideo();
                            }
                        }
                    })
                    .setLockClickListener((view, lock) -> {
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            orientationUtils.setEnable(!lock);
                        }
                    })
                    .setGSYVideoProgressListener((progress, secProgress, currentPosition, duration) -> {
                    })
                    .build(detailPlayer);

            detailPlayer.getFullscreenButton().setOnClickListener(v -> {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(mContext, true, true);
            });
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
    }

    private void requestData() {
        Observable.zip(requestDetail(), requestComment(), (s, s2) -> {
            mDataList = new ArrayList<>();
            if (TextUtils.isEmpty(s) || TextUtils.isEmpty(s2)) return mDataList;
            List<CommentBean> commentList = ParseUtils.parseListData(s2, "content", CommentBean.class);
            ArticleEntity articleEntity = ParseUtils.parseData(s, ArticleEntity.class);
            List<RecommendBean> recommendList = articleEntity.recommendArticles;

            // 填充数据
            fillingData(articleEntity, commentList, recommendList);

            return mDataList;
        }).compose(bindToLifecycle())
                .subscribe(new Observer<List<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Object> objects) {
                        mAdapter.setNewData(objects);
                        loadingLayout.setVisibility(View.GONE);
                        if (objects.isEmpty()) {
                            Toast.makeText(mContext, type == 1 ? "该视频已被删除" : "该文章已被删除", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        handlerApiError(e);
                        loadingLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void handlerApiError(Throwable e) {
        // cao 后台 500
        if (e instanceof ApiException) {
            ParseUtils.handlerApiError((ApiException) e, error -> {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }

    // 添加尾部数据
    private void fillFooterData(ArticleEntity articleEntity) {
        FooterBean footerBean = new FooterBean();
        footerBean.author = articleEntity.author;
        footerBean.source = getNonEmpty(articleEntity.source);
        footerBean.tagNames = articleEntity.tagNames == null ? new ArrayList<>() : articleEntity.tagNames;
        footerBean.gives = articleEntity.gives;
        footerBean.give = articleEntity.give;
        footerBean.id = articleEntity.id;
        footerBean.type = type;
        footerBean.disclaimer = getNonEmpty(articleEntity.disclaimer);
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
        mShareContent = header.fullTitle = type == Constant.Param.FORUM_TYPE ? articleEntity.title : articleEntity.fullTitle;
        header.auditName = mPublishName = getAuditName(articleEntity);
        header.createTime = articleEntity.createTime;
        header.follow = articleEntity.follow;
        header.auditId = type == Constant.Param.FORUM_TYPE ? articleEntity.userId : articleEntity.creatorId;
        header.auditAvatar = getAuditAvatar(articleEntity);
        mDataList.add(header);
    }

    private String getAuditAvatar(ArticleEntity articleEntity) {
        if (type != Constant.Param.FORUM_TYPE && !TextUtils.isEmpty(articleEntity.creatorAvatar)) {
            return articleEntity.creatorAvatar;
        }
        return TextUtils.isEmpty(articleEntity.auditAvatar) ? articleEntity.authorAvatar : articleEntity.auditAvatar;
    }

    private String getAuditName(ArticleEntity articleEntity) {
        if (type != Constant.Param.FORUM_TYPE && !TextUtils.isEmpty(articleEntity.creatorName)) {
            return articleEntity.creatorName;
        }
        return getNonEmpty(TextUtils.isEmpty(articleEntity.auditName) ? articleEntity.author : articleEntity.auditName);
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
        EasyHttp.post((type == Constant.Param.FORUM_TYPE ? ApiUrl.POST_FORUM_PRAISE : ApiUrl.ARTICLE_PRAISE) + footer.id + "/give")
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

                        // 发送事件改变点赞状态
                        EventBus.getDefault().post(new ListPraiseEvent(id, list_position, footer.give));
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
                showDialog(mShareDialog = new BottomShareDialog()
                        .setCollectType(ARoute.ARTICLE_TYPE)
                        .setShareUrl(mShareUrl)
                        .setShareContent(mShareContent)
                        .setGoneOpera(true)
                        .setOnItemClickListener(new BottomShareDialog.OnItemClickListener() {
                            @Override
                            public void onDismiss() {
                                transMask.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCollect(boolean isCollect) {
                            }
                        }));
                transMask.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_more_white:
            case R.id.iv_right_more:
                showDialog(mShareDialog = new BottomShareDialog()
                        .setCollect(isCollect).setArticleId(articleId)
                        .setForumType(type == Constant.Param.FORUM_TYPE)
                        .setShareUrl(mShareUrl)
                        .setShareContent(mShareContent)
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
            loadRootFragment(R.id.fl_comment, mReviewFragment = ReviewFragment.newInstance(id, type, mPublishName));
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
        params.put("collectType", isForum() ? "FORUM" : "ARTICLE");
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
                        Toast.makeText(mContext, isCollect ? "收藏成功" : "取消收藏成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 是否论坛
    private boolean isForum() {
        return type == Constant.Param.FORUM_TYPE;
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
        EasyHttp.post((type == Constant.Param.FORUM_TYPE ? ApiUrl.FORUM_COMMENT_PRAISE : ApiUrl.COMMENT_PRAISE) + id
                + (type == Constant.Param.FORUM_TYPE ? "/give" : ""))
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (type == Constant.Param.FORUM_TYPE) {
                            if (item.given) {
                                item.gives -= 1;
                            } else {
                                item.gives += 1;
                            }
                            item.given = !item.given;
                        } else {
                            if (item.give) {
                                item.gives -= 1;
                            } else {
                                item.gives += 1;
                            }
                            item.give = !item.give;
                        }
                        mAdapter.notifyItemChanged(position);
                    }
                });
    }

    @Override
    public void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        GSYVideoManager.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        getCurPlay().onVideoResume(false);
        super.onResume();
        GSYVideoManager.onResume(false);
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            getCurPlay().release();
        }
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void onBackPressedSupport() {
        if (mReviewFragment != null && mReviewFragment.isShowing()) {
            mReviewFragment.hideBehavior();
            return;
        }

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressedSupport();
    }

    private GSYVideoPlayer getCurPlay() {
        if (detailPlayer.getFullWindowPlayer() != null) {
            return detailPlayer.getFullWindowPlayer();
        }
        return detailPlayer;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshDetailCommentEvent(RefreshDetailCommentEvent event) {
        if (event != null && event.detailId.equals(articleId) && event.entity != null && event.isParent) {
            int addPosition = 0;
            int commentSize = 0;
            boolean isEmptyComment = false;
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                Object obj = mAdapter.getData().get(i);
                if (obj instanceof EmptyCommentBean) {
                    addPosition = i;
                    isEmptyComment = true;
                    break;
                } else if (obj instanceof CommentBean) {
                    addPosition = i;
                    commentSize++;
                } else if (obj instanceof RecommendHeaderBean) {
                    break;
                }
            }

            if (isEmptyComment) {
                mAdapter.notifyItemRemoved(addPosition);
                mAdapter.getData().remove(addPosition);

                event.entity.lastIndex = true;
                mAdapter.getData().add(addPosition, event.entity);
                mAdapter.notifyItemInserted(addPosition);
            } else {
                if (commentSize < 3) {
                    int pos = addPosition + 1 - commentSize;
                    mAdapter.getData().add(pos, event.entity);
                    mAdapter.notifyItemInserted(pos);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshDetailPraiseEvent(RefreshDetailPraiseEvent event) {
        if (event != null && event.articleId.equals(articleId)) {
            int updatePosition = -1;
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                Object obj = mAdapter.getData().get(i);
                if (obj instanceof EmptyCommentBean) {
                    break;
                } else if (obj instanceof CommentBean) {
                    CommentBean bean = (CommentBean) obj;
                    if (bean.id.equals(event.commentId)) {
                        updatePosition = i;
                        if (event.isForum) {
                            bean.given = event.isPraise;
                            if (bean.given) {
                                bean.gives += 1;
                            } else {
                                bean.gives -= 1;
                            }
                        } else {
                            bean.give = event.isPraise;
                            if (bean.give) {
                                bean.gives += 1;
                            } else {
                                bean.gives -= 1;
                            }
                        }
                        break;
                    }
                } else if (obj instanceof RecommendHeaderBean) {
                    break;
                }
            }
            if (updatePosition != -1) mAdapter.notifyItemChanged(updatePosition);
        }
    }

}
