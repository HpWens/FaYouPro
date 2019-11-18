package com.fy.fayou.detail.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
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

    boolean isCollect;
    String articleId;

    OnItemClickListener mListener;

    public BottomShareDialog() {
    }

    public BottomShareDialog setCollect(boolean collect) {
        isCollect = collect;
        return this;
    }

    public BottomShareDialog setArticleId(String articleId) {
        this.articleId = articleId;
        return this;
    }

    public BottomShareDialog setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
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

        mIvCollect.setImageResource(isCollect ? R.mipmap.detail_share_collect_selected : R.mipmap.detail_share_collect);

        mIvCollect.setOnClickListener(v -> {
            requestCollect(articleId);
        });
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
