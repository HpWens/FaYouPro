package com.fy.fayou.update;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fy.fayou.R;
import com.vondear.rxui.view.dialog.RxDialog;

public class ForceUpdateDialog extends RxDialog {

    private TextView mTvSure;
    private TextView mTvCancel;

    public ForceUpdateDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public ForceUpdateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public ForceUpdateDialog(Context context) {
        super(context);
        initView();
    }

    public ForceUpdateDialog(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_force_update, null);
        mTvSure = (TextView) dialogView.findViewById(R.id.tv_sure);
        mTvCancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        setContentView(dialogView);

        setCanceledOnTouchOutside(false);

        mTvCancel.setOnClickListener(v -> {
            if (mListener != null) mListener.onCancel(this);
        });

        mTvSure.setOnClickListener(v -> {
            if (mListener != null) mListener.onSure(this);
        });
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onCancel(ForceUpdateDialog dialog);

        void onSure(ForceUpdateDialog dialog);
    }

    public ForceUpdateDialog setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
        return this;
    }
}
