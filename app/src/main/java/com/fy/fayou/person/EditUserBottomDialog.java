package com.fy.fayou.person;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fy.fayou.R;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxPhotoTool;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.view.dialog.RxDialog;

public class EditUserBottomDialog extends RxDialog {

    private TextView mTvCamera;
    private TextView mTvFile;
    private TextView mTvCancel;
    private TextView mTvLook;

    private boolean mLookLargePic;

    public EditUserBottomDialog(FragmentActivity context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    public EditUserBottomDialog(FragmentActivity context, boolean lookLargePic) {
        super(context);
        mLookLargePic = lookLargePic;
        initView(context);
    }

    public void initView(final FragmentActivity context) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_bottom, null);

        mTvCamera = dialogView.findViewById(R.id.tv_camera);
        mTvFile = dialogView.findViewById(R.id.tv_file);
        mTvCancel = dialogView.findViewById(R.id.tv_cancel);
        mTvLook = dialogView.findViewById(R.id.tv_look);

        mTvLook.setEnabled(mLookLargePic);
        mTvLook.setTextColor(context.getResources().getColor(mLookLargePic ? R.color.color_333333 : R.color.color_e5e5e5));

        mTvCancel.setOnClickListener(arg0 -> cancel());
        mTvCamera.setOnClickListener(arg0 -> {
            // 请求权限
            new RxPermissions(context).request(new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.READ_EXTERNAL_STORAGE})
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            RxPhotoTool.openCameraImage(context);
                            cancel();
                        } else {
                            RxToast.error("请允许拍照权限~");
                            context.startActivity(getAppDetailSettingIntent(context));
                        }
                    });

        });
        mTvFile.setOnClickListener(arg0 -> new RxPermissions(context).request(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE})
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        RxPhotoTool.openLocalImage(context);
                        cancel();
                    } else {
                        RxToast.error("请允许读取本地文件~");
                        context.startActivity(getAppDetailSettingIntent(context));
                    }
                }));

        mTvLook.setOnClickListener(v -> {
            if (mListener != null) mListener.onClick();
            cancel();
        });

        setContentView(dialogView);
        getWindow().setWindowAnimations(R.style.BottomDialog);
        mLayoutParams.gravity = Gravity.BOTTOM;
        mLayoutParams.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
    }

    private Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return localIntent;
    }

    private OnItemListener mListener;

    public interface OnItemListener {
        void onClick();
    }

    public EditUserBottomDialog setOnItemListener(OnItemListener listener) {
        mListener = listener;
        return this;
    }

}
