package com.fy.fayou.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.fy.fayou.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.view.RxToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/login/nickname")
public class NickNameActivity extends BaseActivity {
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.btn_go)
    Button btnGo;
    @BindView(R.id.tv_after)
    TextView tvAfter;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_create_nickname;
    }


    @OnClick({R.id.iv_avatar, R.id.btn_go, R.id.tv_after})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
                // 请求权限
                new RxPermissions(this).request(new String[]{
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE})
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                openAlbum();
                            } else {
                                RxToast.error("请允许拍照权限~");
                                startActivity(getAppDetailSettingIntent(this));
                            }
                        });
                break;
            case R.id.btn_go:
                if (checkNick()) {

                }
                break;
            case R.id.tv_after:

                break;
        }
    }

    private boolean checkNick() {
        String nick = etNickname.getText().toString();
        if (TextUtils.isEmpty(nick)) {
            Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void openAlbum() {
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(true)
                .isCamera(true)
                .glideOverride(160, 160)
                .previewEggs(true)
                .isGif(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null && selectList.size() > 0) {
                        fillAvatar(selectList.get(0));
                    }
                    break;
            }
        }
    }

    private void fillAvatar(LocalMedia media) {
        String path = "";
        if (media.isCut() && !media.isCompressed()) {
            // 裁剪过
            path = media.getCutPath();
        } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
            path = media.getCompressPath();
        } else {
            // 原图
            path = media.getPath();
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.color_f5f5f5)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(this)
                .load(path)
                .apply(options)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(ivAvatar);
    }
}
