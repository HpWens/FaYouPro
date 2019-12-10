package com.fy.fayou.detail.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.UserService;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.base.BaseDialog;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.util.HashMap;

public class BottomShareDialog extends BaseDialog {

    ImageView mIvReport;
    ImageView mIvCollect;
    ImageView mIvNight;
    LinearLayout mReportLayout;

    boolean isCollect;
    String articleId;
    // 是否举报文章
    boolean isReportArticle = true;
    // 是否隐藏举报栏目
    boolean isGoneReport = false;

    boolean isForumType = false;

    OnItemClickListener mListener;

    int collectType = ARoute.ARTICLE_TYPE;

    public BottomShareDialog() {
    }

    public BottomShareDialog setForumType(boolean forumType) {
        isForumType = forumType;
        return this;
    }

    public BottomShareDialog setCollect(boolean collect) {
        isCollect = collect;
        return this;
    }

    public BottomShareDialog setArticleId(String articleId) {
        this.articleId = articleId;
        return this;
    }

    public BottomShareDialog setGoneReport(boolean goneReport) {
        isGoneReport = goneReport;
        return this;
    }

    public BottomShareDialog setCollectType(int collectType) {
        this.collectType = collectType;
        return this;
    }

    public BottomShareDialog setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
        return this;
    }

    public BottomShareDialog setReportArticle(boolean reportArticle) {
        isReportArticle = reportArticle;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), R.style.CreateLiveDialog);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_bottom_share;
    }

    @Override
    protected void initView() {
        mIvReport = findViewById(R.id.iv_report);
        mIvCollect = findViewById(R.id.iv_collect);
        mIvNight = findViewById(R.id.iv_night);
        mReportLayout = findViewById(R.id.report_layout);

        mIvCollect.setImageResource(isCollect ? R.mipmap.detail_share_collect_selected : R.mipmap.detail_share_collect);

        mIvCollect.setOnClickListener(v -> {
            if (UserService.getInstance().checkLoginAndJump()) {
                requestCollect(articleId);
            }
        });

        findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            dismiss();
        });

        mIvReport.setOnClickListener(v -> {
            ARoute.jumpReport(articleId, isReportArticle ? ARoute.REPORT_ARTICLE : ARoute.REPORT_COMMENT, isForumType);
        });

        mReportLayout.setVisibility(!isGoneReport ? View.VISIBLE : View.GONE);

    }

    @Override
    protected void initData() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_in);
        animation.setStartOffset(80);
        mIvReport.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_in);
        animation.setStartOffset(160);
        mIvCollect.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_in);
        animation.setStartOffset(200);
        mIvNight.startAnimation(animation);

        if (collectType != ARoute.ARTICLE_TYPE) {
            // 判定是否收藏
            requestIsCollect(articleId);
        }
    }

    /**
     * @param articleId
     */
    private void requestIsCollect(String articleId) {
        EasyHttp.get(ApiUrl.IS_COLLECT)
                .params("businessId", "" + articleId)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        if (TextUtils.isEmpty(s)) return;
                        isCollect = ParseUtils.getFieldByJson(s, "exist");
                        mIvCollect.setImageResource(isCollect ? R.mipmap.detail_share_collect_selected : R.mipmap.detail_share_collect);
                    }
                });
    }

    // ARTICLE（普法里面的视频和图片，都传这个),合同 （ CONTRACT）  通缉 （ CRIMINAL）    司法解释 （ JUDICIAL）  法律法规（LEGAL）裁判文书（JUDGE） 案例 （ CASE）
    private void requestCollect(String articleId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("businessId", articleId);
        params.put("collectType", isForumType ? "FORUM" : ARoute.getCollectType(collectType));
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
                        mIvCollect.setImageResource(isCollect ? R.mipmap.detail_share_collect_selected : R.mipmap.detail_share_collect);
                        if (mListener != null) {
                            mListener.onCollect(isCollect);
                        }
                    }
                });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mListener != null) mListener.onDismiss();
        super.onDismiss(dialog);
    }

    public interface OnItemClickListener {
        void onDismiss();

        void onCollect(boolean isCollect);
    }
}
