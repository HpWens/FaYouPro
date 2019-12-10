package com.fy.fayou.pufa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.event.ClosePublishNewEvent;
import com.fy.fayou.event.ForumSuccessEvent;
import com.fy.fayou.utils.SoftKeyBoardListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxImageTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/news/publish")
public class PublishNewsActivity extends BaseActivity implements SoftKeyBoardListener.OnSoftKeyBoardChangeListener {

    @Autowired(name = "category_id")
    public String categoryId = "";

    @Autowired(name = "is_forum")
    public boolean isForum = false;

    @Autowired(name = "type")
    public int formType = 0;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    PublishMixingHelper mMixingHelper;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.bottom_layout)
    FrameLayout bottomLayout;
    @BindView(R.id.tv_center_title)
    TextView tvCenterTitle;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
    }

    @Override
    protected void initData() {
        mMixingHelper = new PublishMixingHelper();
        mMixingHelper.init(this, recycler, new PublishMixingHelper.OnMixingListener() {
            @Override
            public void onFocusChange(boolean focus, int position) {
                bottomLayout.setVisibility(position == 0 || !focus ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onEditTextChanged() {
                tvRight.setSelected(!mMixingHelper.isEmpty(false));
            }
        });
        SoftKeyBoardListener.setOnSoftKeyBoardChangeListener(this, this);

        if (isForum) {
            tvRight.setText("发帖");
            tvRight.setTextColor(getResources().getColor(R.color.color_ffffff));
            tvRight.setBackgroundResource(R.drawable.news_submit_selector);
            ((ViewGroup.MarginLayoutParams) tvRight.getLayoutParams()).rightMargin = RxImageTool.dp2px(15);
        }

        tvCenterTitle.setText(isForum ? "发表帖子" : "发布资讯");
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_news_publish;
    }

    @OnClick({R.id.tv_cancel, R.id.tv_right, R.id.iv_add_pic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_right:
                if (!mMixingHelper.isEmpty(true)) {

                    if (isForum) {
                        KtPublishForumHelper helper = new KtPublishForumHelper();
                        helper.handlerArrayData(mContext, categoryId, mMixingHelper.getData(), () -> {
                            Toast.makeText(mContext, "发帖成功", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new ForumSuccessEvent(formType));
                            // 跳转到刑法列表-最新发帖
                            if (formType != ARoute.FORM_PLATE_LIST) {
                                ARoute.jumpPlateList(categoryId, 1);
                            }
                            finish();
                        });
                        return;
                    }
                    // 保存发布资讯数据
                    UserService.getInstance().savePublishNew(mMixingHelper.getData());
                    ARouter.getInstance().build(Constant.NEWS_PUBLISH_NEXT)
                            .withString(Constant.Param.CATEGORY_ID, "" + categoryId)
                            .navigation();
                }
                break;
            case R.id.iv_add_pic:
                startPictureSelector();
                break;
        }
    }

    public void startPictureSelector() {
        int selectedPicNum = mMixingHelper.getPictureNumber();
        int maxSelectNum = 3 - selectedPicNum;
        if (maxSelectNum <= 0) {
            Toast.makeText(mContext, "最多只能插入3张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(maxSelectNum)
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

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClosePublishNewEvent(ClosePublishNewEvent event) {
        if (event != null) {
            finish();
        }
    }
}
