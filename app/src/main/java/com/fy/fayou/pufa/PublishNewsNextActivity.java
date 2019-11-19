package com.fy.fayou.pufa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.fy.fayou.R;
import com.fy.fayou.bean.TagEntity;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.UploadService;
import com.fy.fayou.common.UserService;
import com.fy.fayou.event.ClosePublishNewEvent;
import com.fy.fayou.pufa.adapter.ArticleLabelAdapter;
import com.fy.fayou.pufa.dialog.LabelDialog;
import com.fy.fayou.utils.GlideOption;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxNetTool;
import com.vondear.rxui.view.dialog.RxDialog;
import com.vondear.rxui.view.dialog.RxDialogShapeLoading;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


@Route(path = "/news/publish_next")
public class PublishNewsNextActivity extends BaseActivity {

    @Autowired(name = "category_id")
    public String categoryId = "";

    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_add_cover)
    ImageView ivAddCover;
    @BindView(R.id.et_origin)
    EditText etOrigin;
    @BindView(R.id.et_author)
    EditText etAuthor;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.iv_thumb)
    ImageView ivThumb;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;

    ArticleLabelAdapter mAdapter;
    // 封面路径
    String mThumbPath;
    List<LocalMedia> selectList = new ArrayList<>();

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);

    }

    @Override
    protected void initData() {
        recycler.setLayoutManager(new GridLayoutManager(this, 3));
        recycler.setAdapter(mAdapter = new ArticleLabelAdapter(view -> {
            LabelDialog dialog = new LabelDialog(this, (v, data, labelDialog) -> {
                TagEntity lastTag = new TagEntity();
                data.add(lastTag);
                mAdapter.setNewData(data);
                // 改变提交选中状态
                changeCommitSelection();
                labelDialog.dismiss();
            }, mAdapter.getTags());
            dialog.show();
        }));

        List<TagEntity> list = new ArrayList<>();
        TagEntity entity = new TagEntity();
        list.add(entity);
        mAdapter.setNewData(list);

        etOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                changeCommitSelection();
            }
        });

        etAuthor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                changeCommitSelection();
            }
        });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_news_publish_next;
    }

    @OnClick({R.id.ic_close, R.id.tv_right, R.id.iv_add_cover, R.id.iv_thumb, R.id.iv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_close:
                finish();
                break;
            case R.id.tv_right:
                if (!verifyEmpty(true)) {
                    requestPublish();
                }
                break;
            case R.id.iv_thumb:
                PictureSelector.create(this).themeStyle(R.style.picture_default_style).openExternalPreview(0, selectList);
                break;
            case R.id.iv_add_cover:
                startPictureSelector();
                break;
            case R.id.iv_delete:
                mThumbPath = "";
                ivAddCover.setVisibility(View.VISIBLE);
                ivThumb.setVisibility(View.GONE);
                ivDelete.setVisibility(View.GONE);
                break;
        }
    }

    private boolean verifyEmpty() {
        return verifyEmpty(false);
    }

    private boolean verifyEmpty(boolean hint) {
//        if (TextUtils.isEmpty(mThumbPath)) {
//            Toast.makeText(mContext, "请选中封面", Toast.LENGTH_SHORT).show();
//            return true;
//        }
        String source = etOrigin.getText().toString();
        if (TextUtils.isEmpty(source)) {
            if (hint) {
                Toast.makeText(mContext, "请输入文章来源", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
//        String author = etAuthor.getText().toString();
//        if (TextUtils.isEmpty(author)) {
//            return true;
//        }
        if (mAdapter.getTags().isEmpty()) {
            if (hint) {
                Toast.makeText(mContext, "请添加文章标签", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }

    private void changeCommitSelection() {
        tvRight.setSelected(!verifyEmpty());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (!selectList.isEmpty()) {
                        mThumbPath = selectList.get(0).getPath();

                        if (ivThumb.getVisibility() != View.VISIBLE) {
                            ivThumb.setVisibility(View.VISIBLE);
                            ivDelete.setVisibility(View.VISIBLE);
                            ivAddCover.setVisibility(View.GONE);
                        }
                        Glide.with(mContext)
                                .load(mThumbPath)
                                .apply(GlideOption.getRadiusOption(150, 100, 2))
                                .into(ivThumb);

                        changeCommitSelection();
                    }
                    break;
            }
        }
    }

    public void startPictureSelector() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)
                .previewImage(true)
                .isCamera(true)
                .glideOverride(160, 160)
                .previewEggs(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    // 请求发布接口
    private void requestPublish() {
        boolean isAvailable = RxNetTool.isAvailable(this);
        if (!isAvailable) {
            Toast.makeText(mContext, "请检查您的网络连接", Toast.LENGTH_SHORT).show();
            return;
        }

        RxDialogShapeLoading dialog = new RxDialogShapeLoading(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        // 获取到编辑资讯数据
        List<Object> dataList = UserService.getInstance().getPublishNew();
        if (dataList == null || dataList.isEmpty()) {
            dialog.dismiss();
            return;
        }

        UploadService.getInstance().uploadPublishImages(dataList, new UploadService.OnPublishUploadListener() {
            @Override
            public void onSuccess(final List<Object> list) {
                if (!TextUtils.isEmpty(mThumbPath)) {
                    // 上传封面
                    UploadService.getInstance().uploadSingleFile(mThumbPath, new UploadService.OnUploadListener() {
                        @Override
                        public void onSuccess(String key) {
                            requestPublish(list, key, dialog);
                        }

                        @Override
                        public void onFailure(String error) {
                            failureHint(dialog);
                        }
                    });
                } else {
                    // 无封面
                    requestPublish(list, "", dialog);
                }
            }

            @Override
            public void onFailure(String error) {
                failureHint(dialog);
            }
        });
    }

    private void failureHint(RxDialog dialog) {
        Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
        dismissUploadDialog(dialog);
    }

    private void dismissUploadDialog(RxDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void requestPublish(List<Object> data, String thumbHttp, RxDialog dialog) {
        // 组装 content 数据
        String source = etOrigin.getText().toString();
        String author = etAuthor.getText().toString();

        String title = "";
        StringBuilder sb = new StringBuilder();
        for (Object obj : data) {
            if (obj instanceof TitleEntity) {
                TitleEntity titleEntity = (TitleEntity) obj;
                title = titleEntity.name;
            } else if (obj instanceof TextEntity) {
                TextEntity textEntity = (TextEntity) obj;
                if (!TextUtils.isEmpty(textEntity.content)) {
                    sb.append(textEntity.content + " ");
                }
            } else if (obj instanceof PicEntity) {
                PicEntity picEntity = (PicEntity) obj;
                sb.append("![](" + picEntity.httpPath + ")" + " ");
            }
        }

        StringBuilder tags = new StringBuilder();
        for (TagEntity tagEntity : mAdapter.getTags()) {
            tags.append(tagEntity.id + ",");
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("categoryId", categoryId);
        params.put("fullTitle", "" + title);
        params.put("cover", thumbHttp);
        params.put("source", source);
        params.put("description", "");
        params.put("content", "" + sb.toString());
        params.put("tagIdsStr", tags.substring(0, tags.length() - 1));
        params.put("author", author);
        params.put("showIndex", false);
        params.put("articleType", "ARTICLE");
        JSONObject jsonObject = new JSONObject(params);

        EasyHttp.post(ApiUrl.HOME_ARTICLE)
                .upJson(jsonObject.toString())
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        failureHint(dialog);
                    }

                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(mContext, "上次成功", Toast.LENGTH_SHORT).show();
                        dismissUploadDialog(dialog);

                        // 清理编辑数据
                        UserService.getInstance().clearPublishNew();
                        // 关闭编辑页面
                        EventBus.getDefault().post(new ClosePublishNewEvent());
                        finish();
                    }
                });
    }
}
