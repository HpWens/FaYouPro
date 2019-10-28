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

import io.reactivex.functions.Consumer;

public class EditUserBottomDialog extends RxDialog {

    private TextView mTvCamera;
    private TextView mTvFile;
    private TextView mTvCancel;

    public EditUserBottomDialog(FragmentActivity context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    public EditUserBottomDialog(FragmentActivity context) {
        super(context);
        initView(context);
    }

    public void initView(final FragmentActivity context) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_bottom, null);

        mTvCamera = dialogView.findViewById(com.vondear.rxui.R.id.tv_camera);
        mTvFile = dialogView.findViewById(com.vondear.rxui.R.id.tv_file);
        mTvCancel = dialogView.findViewById(com.vondear.rxui.R.id.tv_cancel);

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                cancel();
            }
        });
        mTvCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
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

            }
        });
        mTvFile.setOnClickListener(arg0 -> new RxPermissions(context).request(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE})
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            RxPhotoTool.openLocalImage(context);
                            cancel();
                        } else {
                            RxToast.error("请允许读取本地文件~");
                            context.startActivity(getAppDetailSettingIntent(context));
                        }
                    }
                }));

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

}
