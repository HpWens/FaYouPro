package com.fy.fayou.person;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.UploadService;
import com.fy.fayou.common.UserService;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxPhotoTool;
import com.vondear.rxtool.RxSPTool;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/person/edit")
public class EditUserActivity extends BaseActivity {

    @BindView(R.id.iv_header)
    CircleImageView ivAvatar;
    @BindView(R.id.et_name)
    EditText etName;

    private String avatarPath = "";

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("编辑资料");
        setLeftBackListener(v -> finish()).setRightTextListener(v -> {
            String nickName = etName.getText().toString();
            if (TextUtils.isEmpty(nickName)) {
                Toast.makeText(mContext, "请输入昵称", Toast.LENGTH_SHORT).show();
                return;
            }

            UploadService.getInstance().uploadSingleFile(avatarPath, new UploadService.OnUploadListener() {
                @Override
                public void onSuccess(String key) {
                    requestNick(key, nickName);
                }

                @Override
                public void onFailure(String error) {

                }
            });

        });
    }

    @Override
    protected void initData() {
        avatarPath = UserService.getInstance().getAvatar();
        if (!TextUtils.isEmpty(avatarPath)) {
            Glide.with(mContext).
                    load(avatarPath).
                    apply(getAvatarOption()).
                    thumbnail(0.5f).
                    into(ivAvatar);
        }
        String nick = UserService.getInstance().getNickName();
        if (!TextUtils.isEmpty(nick)) {
            etName.setText(nick);
        }
    }

    private void requestNick(String avatar, final String nick) {
        HashMap<String, String> params = new HashMap<>();
        params.put("nickName", nick);
        params.put("avatar", avatar);
        JSONObject jsonObject = new JSONObject(params);

        EasyHttp.post(ApiUrl.USER_UPDATE)
                .upJson(jsonObject.toString())
                .execute(new SimpleCallBack<String>() {

                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        // 更新数据
                        UserService.getInstance().setAvatar(avatar);
                        UserService.getInstance().setNickName(nick);
                        finish();
                    }
                });
    }

    @OnClick(R.id.iv_header)
    public void onClick() {
        boolean isLook = !TextUtils.isEmpty(avatarPath);
        EditUserBottomDialog dialog = new EditUserBottomDialog(mContext, isLook).setOnItemListener(() -> {
            if (!TextUtils.isEmpty(avatarPath)) previewPic();
        });
        dialog.show();
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_user_edit;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE:
                // 选择相册之后的处理
                if (resultCode == RESULT_OK) {
                    // RxPhotoTool.cropImage(ActivityUser.this, );// 裁剪图片
                    initUCrop(data.getData());
                }

                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                    /* data.getExtras().get("data");*/
                    //  RxPhotoTool.cropImage(ActivityUser.this, RxPhotoTool.imageUriFromCamera);// 裁剪图片
                    initUCrop(RxPhotoTool.imageUriFromCamera);
                }

                break;
            case RxPhotoTool.CROP_IMAGE://普通裁剪后的处理
                RequestOptions options = new RequestOptions()
                        .placeholder(R.color.color_e5e5e5)
                        //异常占位图(当加载异常的时候出现的图片)
                        .error(R.color.color_e5e5e5)
                        //禁止Glide硬盘缓存缓存
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

                Glide.with(mContext).
                        load(RxPhotoTool.cropImageUri).
                        apply(options).
                        thumbnail(0.5f).
                        into(ivAvatar);
                // RequestUpdateAvatar(new File(RxPhotoTool.getRealFilePath(mContext, RxPhotoTool.cropImageUri)));
                break;

            case UCrop.REQUEST_CROP:
                // UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    Uri resultUri = UCrop.getOutput(data);
                    roadImageView(resultUri, ivAvatar);
                    RxSPTool.putContent(mContext, "AVATAR", resultUri.toString());

                    avatarPath = resultUri.getPath();
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
                break;
            case UCrop.RESULT_ERROR:
                // UCrop裁剪错误之后的处理
                final Throwable cropError = UCrop.getError(data);

                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 预览图片
    private void previewPic() {
        LocalMedia media = new LocalMedia();
        media.setPath(avatarPath);
        List<LocalMedia> list = new ArrayList<>();
        list.add(media);
        PictureSelector.create(this).themeStyle(R.style.picture_default_style).openExternalPreview(0, list);
    }

    private void initUCrop(Uri uri) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), imageName + ".jpeg"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark));

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
        //options.setCircleDimmedLayer(true);
        //设置是否展示矩形裁剪框
        // options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        //options.setCropGridColumnCount(2);
        //设置横线的数量
        //options.setCropGridRowCount(1);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this);
    }

    // 从Uri中加载图片 并将其转化成File文件返回
    private File roadImageView(Uri uri, ImageView imageView) {
        RequestOptions options = getAvatarOption();

        Glide.with(mContext).
                load(uri).
                apply(options).
                thumbnail(0.5f).
                into(imageView);

        return (new File(RxPhotoTool.getImageAbsolutePath(this, uri)));
    }

    private RequestOptions getAvatarOption() {
        return new RequestOptions()
                .placeholder(R.color.color_e5e5e5)
                //异常占位图(当加载异常的时候出现的图片)
                .error(R.color.color_e5e5e5)
                .transform(new CircleCrop())
                //禁止Glide硬盘缓存缓存
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    }
}
