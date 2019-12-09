package com.fy.fayou.forum.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.event.ForumNewTabEvent;
import com.fy.fayou.event.ForumSuccessEvent;
import com.fy.fayou.forum.adapter.ForumListVPAdapter;
import com.fy.fayou.forum.bean.PlateEntity;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.ParseUtils;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxImageTool;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/forum/list")
public class ForumListActivity extends BaseActivity {

    @Autowired(name = "id")
    public String id = "";

    @Autowired(name = "position")
    public int position = 0;

    public String plateName = "";

    private final String[] mTitles = {
            "最新回复", "最新发帖", "热门"
    };

    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_follow_num)
    TextView tvFollowNum;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.iv_avatar_first)
    CircleImageView ivAvatarFirst;
    @BindView(R.id.iv_avatar_second)
    CircleImageView ivAvatarSecond;
    @BindView(R.id.iv_avatar_three)
    CircleImageView ivAvatarThree;
    @BindView(R.id.more_plate_layout)
    LinearLayout morePlateLayout;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;

    ForumListVPAdapter mAdapter;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setRightMoreRes(R.mipmap.forum_publish_black_ic).setLeftBackListener(v -> {
            finish();
        }).setRightMoreListener(v -> {
            ARoute.jumpPublishFromPlateList(id);
        });
    }

    @Override
    protected void initData() {
        EasyHttp.get(ApiUrl.FORUM_PLATE_LIST + id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            PlateEntity entity = ParseUtils.parseData(s, PlateEntity.class);

                            fillingData(entity);
                        }
                    }
                });


    }

    /**
     * @param entity
     */
    private void fillingData(PlateEntity entity) {

        Glide.with(mContext)
                .load(getNonEmpty(entity.logo))
                .apply(GlideOption.getItemOptionByWH(60, 60))
                .into(ivHeader);

        tvName.setText(getNonEmpty(plateName = entity.name));
        tvDesc.setText(getNonEmpty(entity.description));
        tvFollowNum.setText(entity.comments + "讨论·" + entity.follows + "关注");

        switchFollowStyle(entity.followed);

        tvFollow.setOnClickListener(v -> {
            requestFollowPlate(entity);
        });

        if (entity.starUserList != null) {
            int size = entity.starUserList.size();
            for (int i = 0; i < (size > 3 ? 3 : size); i++) {
                Glide.with(mContext)
                        .load(getNonEmpty(entity.starUserList.get(i).avatar))
                        .apply(GlideOption.getAvatarOption(RxImageTool.dp2px(20), RxImageTool.dp2px(20)))
                        .into(i == 0 ? ivAvatarFirst : (i == 1 ? ivAvatarSecond : ivAvatarThree));
            }
            if (size == 2) {
                ivAvatarThree.setVisibility(View.GONE);
            } else if (size == 1) {
                ivAvatarSecond.setVisibility(View.GONE);
                ivAvatarThree.setVisibility(View.GONE);
            }
        }

        viewpager.setAdapter(mAdapter = new ForumListVPAdapter(getSupportFragmentManager(), id, mTitles, entity.indexPostList));
        tab.setViewPager(viewpager);
        if (position != 0) tab.setCurrentTab(position);
        mAdapter.setOnScrollClashListener(isScroll -> viewpager.setScroll(isScroll));
    }

    private void switchFollowStyle(boolean isFollowed) {
        tvFollow.setBackgroundResource(isFollowed ? R.drawable.my_unfollow_round : R.drawable.my_follow_red_round);
        tvFollow.setTextColor(getResources().getColor(isFollowed ? R.color.color_d2d2d2 : R.color.color_ffffff));
        tvFollow.setText(isFollowed ? "已关注" : "+关注");
    }

    /**
     * @param item
     */
    private void requestFollowPlate(PlateEntity item) {
        HashMap<String, String> params = new HashMap<>();
        params.put("boardId", item.id);
        params.put("followed", !item.followed + "");
        JSONObject jsonObject = new JSONObject(params);

        EasyHttp.put(ApiUrl.FORUM_FOLLOW_PLATE)
                .upJson(jsonObject.toString())
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        item.followed = !item.followed;
                        switchFollowStyle(item.followed);
                    }
                });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_forum_detail;
    }

    @OnClick({R.id.more_plate_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_plate_layout:
                ARoute.jumpStarPlate(id, plateName);
                break;
        }
    }

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    /**
     * 发帖成功
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onForumSuccessEvent(ForumSuccessEvent event) {
        if (event != null && event.fromType == ARoute.FORM_PLATE_LIST) {
            if (mAdapter != null && tab != null && tab.getTabCount() > 1) {
                tab.setCurrentTab(1);
                EventBus.getDefault().post(new ForumNewTabEvent());
            }
        }
    }

}
