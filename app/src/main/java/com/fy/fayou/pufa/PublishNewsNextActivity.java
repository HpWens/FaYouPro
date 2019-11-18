package com.fy.fayou.pufa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.fy.fayou.R;
import com.fy.fayou.pufa.adapter.ArticleLabelAdapter;
import com.fy.fayou.pufa.bean.LabelEntity;
import com.fy.fayou.pufa.dialog.LabelDialog;
import com.fy.fayou.utils.GlideOption;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


@Route(path = "/news/publish_next")
public class PublishNewsNextActivity extends BaseActivity {

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
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);

    }

    @Override
    protected void initData() {
        recycler.setLayoutManager(new GridLayoutManager(this, 3));
        recycler.setAdapter(mAdapter = new ArticleLabelAdapter(view -> {
            LabelDialog dialog = new LabelDialog(this);
            dialog.show();
        }));

        List<LabelEntity> list = new ArrayList<>();
        LabelEntity entity = new LabelEntity();
        list.add(entity);
        mAdapter.setNewData(list);
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
}
