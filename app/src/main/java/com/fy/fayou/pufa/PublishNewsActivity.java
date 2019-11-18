package com.fy.fayou.pufa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.R;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.utils.SoftKeyBoardListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/news/publish")
public class PublishNewsActivity extends BaseActivity implements SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
    @BindView(R.id.recycler)
    RecyclerView recycler;

    PublishMixingHelper mMixingHelper;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.bottom_layout)
    FrameLayout bottomLayout;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
    }

    @Override
    protected void initData() {
        mMixingHelper = new PublishMixingHelper();
        mMixingHelper.init(this, recycler, (focus, position) -> {
            bottomLayout.setVisibility(position == 0 || !focus ? View.GONE : View.VISIBLE);
        });
        SoftKeyBoardListener.setOnSoftKeyBoardChangeListener(this, this);
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_news_publish;
    }

    @OnClick({R.id.tv_cancel, R.id.tv_right, R.id.iv_add_pic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                break;
            case R.id.tv_right:
                if (!mMixingHelper.isEmpty()) {
                    // 保存发布资讯数据
                    UserService.getInstance().savePublishNew(mMixingHelper.getData());
                    ARouter.getInstance().build(Constant.NEWS_PUBLISH_NEXT).navigation();
                }
                break;
            case R.id.iv_add_pic:
                startPictureSelector();
                break;
        }
    }

    public void startPictureSelector() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(9)
                .previewImage(true)
                .isCamera(true)
                .glideOverride(160, 160)
                .previewEggs(true)
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
                    mMixingHelper.addMultiPicture(mMixingHelper.covertStringArray(selectList));
                    break;
            }
        }
    }

    @Override
    public void keyBoardShow(int height) {

    }

    @Override
    public void keyBoardHide(int height) {
        if (bottomLayout != null) {
            bottomLayout.setVisibility(View.GONE);
        }
    }
}
