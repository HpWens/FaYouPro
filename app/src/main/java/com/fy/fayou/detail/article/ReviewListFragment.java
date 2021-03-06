package com.fy.fayou.detail.article;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiResult;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.detail.adapter.ReviewAdapter;
import com.fy.fayou.detail.bean.CommentBean;
import com.fy.fayou.event.RefreshDetailPraiseEvent;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.adapter.BaseMultiAdapter;
import com.meis.base.mei.base.BaseMultiListFragment;
import com.meis.base.mei.constant.DataConstants;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.status.ViewState;
import com.vondear.rxtool.RxTimeTool;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class ReviewListFragment extends BaseMultiListFragment<CommentBean> {

    private RecyclerView mRecyclerView;
    private ReviewAdapter mAdapter;

    // 文章id
    private String articleId = "0";
    // 父评论id
    private String parentId = "0";

    private boolean isForum;
    private boolean isDesc = true;

    public static final String ARTICLE_ID = "article_id";
    private OnItemClickListener mListener;

    @Override
    protected void initView() {
        if (getArguments() != null) {
            articleId = getArguments().getString(ARTICLE_ID, "");
            isForum = getArguments().getBoolean(Constant.Param.IS_FORUM, false);
        }
        super.initView();
    }

    public static ReviewListFragment newInstance(String articleId, boolean isForum) {
        Bundle args = new Bundle();
        args.putString(ARTICLE_ID, articleId);
        args.putBoolean(Constant.Param.IS_FORUM, isForum);
        ReviewListFragment fragment = new ReviewListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = getView().findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRecyclerView;
    }

    // 滚动到顶部
    public void scrollTop() {
        if (mRecyclerView != null) mRecyclerView.scrollToPosition(0);
    }

    @Override
    protected BaseMultiAdapter<CommentBean> getAdapter() {
        return mAdapter = new ReviewAdapter(articleId, isForum, new ReviewAdapter.OnClickListener() {
            @Override
            public void onPraise(View v, int pos, CommentBean item) {
                if (UserService.getInstance().checkLoginAndJump()) {
                    requestPraise(item.id, pos, item);
                }
            }

            @Override
            public void onComment(String userName, String articleId, String parentId, int position, String userId) {
                if (mListener != null) {
                    mListener.onClick(userName, articleId, parentId, position, userId);
                }
            }

            @Override
            public void onLoadMoreComment(List<String> excludeIds, String parentId, int position, int helperPage) {
                requestMoreComment(excludeIds, parentId, position, helperPage);
            }

            @Override
            public void onJumpSecondComment(String parentId, CommentBean parent) {
                if (mListener != null) mListener.onJumpSecondReview(parentId, parent);
            }
        });
    }

    /**
     * @param excludeIds
     * @param parentId
     */
    private void requestMoreComment(List<String> excludeIds, String parentId, int position, int helperPage) {
        StringBuilder sb = new StringBuilder();
        for (String ids : excludeIds) {
            sb.append(ids + ",");
        }
        EasyHttp.get(ApiUrl.FIND_COMMENT_MORE)
                .params("excludeIds", sb.substring(0, sb.length() - 1))
                .params("parentId", parentId)
                .params("size", "4")
                .params("startTime", RxTimeTool.getCurTimeString())
                .params("page", "" + helperPage)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            ApiResult<CommentBean> apiResult = ParseUtils.fromJsonApiResult(s, CommentBean.class);
                            if (apiResult != null && apiResult.content != null) {
                                if (apiResult.content.isEmpty()) return;
                                for (CommentBean entity : apiResult.content) {
                                    entity.helperId = parentId;
                                    entity.level = 1;
                                }
                                CommentBean expand = mAdapter.getData().get(position);
                                expand.level = 2;
                                expand.helperExpandNumber += apiResult.content.size();
                                expand.laveCommentCount = expand.helperChildCount - expand.helperExpandNumber;
                                expand.helperPage = apiResult.page + 1;

                                mAdapter.notifyItemChanged(position);

                                mAdapter.getData().addAll(position, apiResult.content);
                                mAdapter.notifyItemRangeInserted(position, apiResult.content.size());
                            }
                        }
                    }
                });
    }

    @Override
    protected Observable<Result<List<CommentBean>>> getListObservable(int pageNo) {
        Observable<String> observable = null;
        if (isForum) {
            observable = EasyHttp.get(ApiUrl.GET_FORUM_FIRST_COMMENT)
                    .params("postId", articleId)
                    .params("page", (pageNo - 1) + "")
                    .params("size", "20")
                    .params("orderBy", isDesc ? "createTimeDesc" : "createTimeAsc")
                    .execute(String.class);
        } else {
            observable = EasyHttp.get(ApiUrl.COMMENT_LIST)
                    .params("articleId", articleId)
                    .params("parentId", parentId)
                    .params("page", (pageNo - 1) + "")
                    .params("size", "20")
                    .execute(String.class);
        }
        return getListByField(observable, "content");
    }

    public void setDesc(boolean desc) {
        isDesc = desc;
        loadPage(DataConstants.FIRST_PAGE);
    }

    public boolean isDesc() {
        return isDesc;
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
        return R.layout.comm_recycler;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    protected Observable<Result<List<CommentBean>>> getListByField(Observable<String> observable, final String field) {
        final Result<List<CommentBean>> result = new Result<>();
        return observable.map(s -> {
            if (!TextUtils.isEmpty(s)) {
                JSONObject json = new JSONObject(s);
                if (json.has("count")) {
                    int count = json.optInt("count");
                    // if (isForum) mListener.onTotalCommentNumber(count);
                }
                if (json != null && field != null && json.has(field)) {
                    List<CommentBean> list = ParseUtils.parseListData(json.optString(field), CommentBean.class);
                    result.data = new ArrayList<>();
                    for (CommentBean bean : list) {
                        bean.level = 0;
                        result.data.add(bean);
                        if (bean.childrenList != null && !bean.childrenList.isEmpty()) {
                            int childSize = bean.childrenList.size();
                            for (int i = 0; i < childSize; i++) {
                                CommentBean child = bean.childrenList.get(i);
                                child.level = 1;
                                child.childIndex = i;
                                child.lastIndex = i == (childSize - 1);
                                child.laveCommentCount = child.lastIndex ? bean.comments - childSize : 0;
                                child.helperPage = 0;
                                child.helperId = bean.id;
                                child.helperChildCount = bean.comments;
                                result.data.add(child);
                            }

                            // 添加展示更多
                            CommentBean expand = new CommentBean();
                            expand.level = 2;
                            expand.id = bean.id;
                            expand.laveCommentCount = bean.comments - childSize;
                            expand.helperChildCount = bean.comments;
                            expand.helperExpandNumber = childSize;
                            expand.helperId = bean.id;
                            expand.helperPage = 0;
                            expand.helperParent = bean;
                            result.data.add(expand);

                        }
                    }
                }
            }
            return result;
        });
    }

    /**
     * 请求点赞
     *
     * @param id
     * @param item
     */
    private void requestPraise(String id, int position, CommentBean item) {
        EasyHttp.post((isForum ? ApiUrl.FORUM_COMMENT_PRAISE : ApiUrl.COMMENT_PRAISE) + id + (isForum ? "/give" : ""))
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        ParseUtils.handlerApiError(e, error -> {
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onSuccess(String s) {
                        boolean isPraise;
                        if (isForum) {
                            if (item.given) {
                                item.gives -= 1;
                            } else {
                                item.gives += 1;
                            }
                            isPraise = item.given = !item.given;
                        } else {
                            if (item.give) {
                                item.gives -= 1;
                            } else {
                                item.gives += 1;
                            }
                            isPraise = item.give = !item.give;
                        }
                        mAdapter.notifyItemChanged(position);

                        EventBus.getDefault().post(new RefreshDetailPraiseEvent(articleId, id, isForum, isPraise));
                    }
                });
    }

    // 更新数据
    public void updateData(boolean isParent, int position, CommentBean entity) {
        if (mAdapter == null) return;
        if (mAdapter.getData().isEmpty()) setState(ViewState.COMPLETED);
        if (isParent) {
            mAdapter.getData().add(0, entity);
            mAdapter.notifyItemInserted(0);
            mRecyclerView.smoothScrollToPosition(0);
        } else {
            if (mAdapter.getData().size() > position) {
                entity.level = 1;
                int insert = getInsertPosition(position);
                mAdapter.getData().add(insert, entity);
                mAdapter.notifyItemInserted(insert);

                // 滚动
                mRecyclerView.postDelayed(() -> {
                    mRecyclerView.smoothScrollToPosition(insert);
                }, 400);
            }
        }
    }

    // 获取到插入的位置
    private int getInsertPosition(int position) {
        if (mAdapter.getData().get(position).level == 0) {
            position += 1;
        }

        if (position == mAdapter.getData().size()) {
            return position;
        }

        if (mAdapter.getData().get(position).level == 0) {
            return position;
        } else {
            for (int i = position; i < mAdapter.getData().size(); i++) {
                CommentBean entity = mAdapter.getData().get(i);
                if (entity.level == 0 || entity.level == 2) {
                    return i;
                }
            }
        }
        return mAdapter.getData().size();
    }

    public interface OnItemClickListener {
        void onClick(String userName, String articleId, String parentId, int position, String reUserId);

        void onTotalCommentNumber(int count);

        void onJumpSecondReview(String id, CommentBean parent);
    }

    public ReviewListFragment setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
        return this;
    }
}
