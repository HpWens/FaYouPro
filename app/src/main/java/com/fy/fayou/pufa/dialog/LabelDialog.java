package com.fy.fayou.pufa.dialog;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.fy.fayou.R;
import com.fy.fayou.bean.TagEntity;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.utils.ParseUtils;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxui.view.dialog.RxDialog;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

public class LabelDialog extends RxDialog {

    RecyclerView mRecyclerView;
    LabelAdapter mAdapter;

    OnTagListener mListener;
    List<TagEntity> mTagList = new ArrayList<>();

    public LabelDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public LabelDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public LabelDialog(Context context, OnTagListener listener, List<TagEntity> tags) {
        super(context);
        this.mTagList = tags;
        this.mListener = listener;
        initView();
    }

    public LabelDialog(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_article_publish_label, null);
        mRecyclerView = dialogView.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setAdapter(mAdapter = new LabelAdapter());
        mAdapter.setSelectedTags(mTagList);
        setContentView(dialogView);

        mLayoutParams.width = (int) (RxDeviceTool.getScreenWidth(getContext()) * 0.8f);

        findViewById(R.id.iv_close).setOnClickListener(v -> {
            dismiss();
        });

        findViewById(R.id.tv_ok).setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onTag(v, mAdapter.getSelectedTags(), this);
            }
        });

        requestTag();
    }

    /**
     * 请求标签
     */
    private void requestTag() {
        EasyHttp.get(ApiUrl.FIND_ARTICLE_TAG).execute(new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(String s) {
                if (!TextUtils.isEmpty(s)) {
                    List<TagEntity> data = ParseUtils.parseListData(s, TagEntity.class);
                    mAdapter.setNewData(data);
                }
            }
        });
    }

    public interface OnTagListener {
        void onTag(View v, List<TagEntity> data, LabelDialog dialog);
    }

}
