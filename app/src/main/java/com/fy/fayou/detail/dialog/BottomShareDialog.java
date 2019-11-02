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
import com.meis.base.mei.base.BaseDialog;

public class BottomShareDialog extends BaseDialog {

    ImageView mIvReport;
    ImageView mIvCollect;
    ImageView mIvNight;

    OnItemClickListener mListener;

    public BottomShareDialog() {
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mListener != null) mListener.onDismiss();
        super.onDismiss(dialog);
    }

    public interface OnItemClickListener {
        void onDismiss();
    }
}
