package com.fy.fayou.detail.dialog;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.fy.fayou.R;
import com.meis.base.mei.base.BaseDialog;
import com.vondear.rxtool.RxKeyboardTool;

public class BottomCommentDialog extends BaseDialog {

    EditText mEtHint;

    public BottomCommentDialog() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_bottom_comment;
    }

    @Override
    protected void initView() {
        mEtHint = findViewById(R.id.et_hint);
        mEtHint.post(() -> {
            mEtHint.requestFocus();
            RxKeyboardTool.showSoftInput(getActivity(), mEtHint);
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 显示在底部
        params.gravity = Gravity.BOTTOM;
        // 宽度填充满屏
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //显示dialog的时候,就显示软键盘
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        window.setAttributes(params);

        // 这里用透明颜色替换掉系统自带背景
        int color = ContextCompat.getColor(getActivity(), android.R.color.transparent);
        window.setBackgroundDrawable(new ColorDrawable(color));
    }

}
