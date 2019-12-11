package com.fy.fayou.my.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.bean.UserInfo;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.my.adapter.UserCenterVPAdapter;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.ParseUtils;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseFragment;
import com.vondear.rxtool.RxImageTool;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserCenterFragment extends BaseFragment {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.tv_follow_num)
    TextView tvFollowNum;
    @BindView(R.id.tv_fan_num)
    TextView tvFanNum;
    @BindView(R.id.tv_praise_num)
    TextView tvPraiseNum;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_top_avatar)
    ImageView ivTopAvatar;
    @BindView(R.id.tv_top_name)
    TextView tvTopName;
    @BindView(R.id.tv_top_follow)
    TextView tvTopFollow;
    @BindView(R.id.top_header_layout)
    LinearLayout topHeaderLayout;

    Unbinder unbinder;

    private int maxOffset;
    private final String[] mTitles = {
            "资讯", "帖子"
    };

    // 是否关注
    private boolean isFollow;
    private String userId = "";

    UserCenterVPAdapter mAdapter;

    public static UserCenterFragment newInstance(String userId) {
        Bundle args = new Bundle();
        args.putString(Constant.Param.USER_ID, userId);
        UserCenterFragment fragment = new UserCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            userId = getArguments().getString(Constant.Param.USER_ID, "");
        }
        unbinder = ButterKnife.bind(this, getView());

        maxOffset = -RxImageTool.dip2px(116);

        viewpager.setAdapter(mAdapter = new UserCenterVPAdapter(getChildFragmentManager(), mTitles, userId));
        tab.setViewPager(viewpager);

        mAdapter.setOnScrollClashListener(isScroll -> viewpager.setScroll(isScroll));

        appBar.addOnOffsetChangedListener((appBarLayout, i) -> {
            topHeaderLayout.setAlpha(((float) i / maxOffset));
        });
    }

    @Override
    protected void initData() {

        requestUserData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_center;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_back, R.id.tv_follow, R.id.iv_close, R.id.tv_top_follow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.tv_top_follow:
            case R.id.tv_follow:
                if (UserService.getInstance().checkLoginAndJump()) {
                    requestFollowOrCancel();
                }
                break;
        }
    }

    // 请求用户数据
    private void requestUserData() {
        EasyHttp.get(ApiUrl.USER_INFO + userId)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            UserInfo user = ParseUtils.parseData(s, UserInfo.class);
                            fillingData(user);
                        }
                    }
                });
    }

    /**
     * 填充数据
     *
     * @param user
     */
    private void fillingData(UserInfo user) {
        if (user == null) return;
        isFollow = user.follow;
        tvName.setText(getNonEmpty(user.nickName));
        Glide.with(getActivity())
                .load(getNonEmpty(user.avatar))
                .apply(GlideOption.getAvatarOption(RxImageTool.dp2px(60), RxImageTool.dp2px(60)))
                .into(ivAvatar);

        tvFollow.setText(isFollow ? "已关注" : "+关注");

        tvTopFollow.setBackgroundResource(isFollow ? R.drawable.my_unfollow_round : R.drawable.my_follow_red_round);
        tvTopFollow.setTextColor(getActivity().getResources().getColor(isFollow ? R.color.color_d2d2d2 : R.color.color_ffffff));
        tvTopFollow.setText(isFollow ? "已关注" : "+关注");

        tvTopName.setText(getNonEmpty(user.nickName));
        Glide.with(getActivity())
                .load(getNonEmpty(user.avatar))
                .apply(GlideOption.getAvatarOption(RxImageTool.dp2px(30), RxImageTool.dp2px(30)))
                .into(ivTopAvatar);

        tvFollowNum.setText(user.followings + " 关注");
        tvFanNum.setText(user.followers + " 粉丝");
        tvPraiseNum.setText(user.gives + " 获赞");
    }

    private void requestFollowOrCancel() {
        EasyHttp.post(isFollow ? ApiUrl.UN_FOLLOW_USER : ApiUrl.FOLLOW_USER)
                .upJson(userId)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        isFollow = !isFollow;

                        tvFollow.setText(isFollow ? "已关注" : "+关注");

                        tvTopFollow.setBackgroundResource(isFollow ? R.drawable.my_unfollow_round : R.drawable.my_follow_red_round);
                        tvTopFollow.setTextColor(getActivity().getResources().getColor(isFollow ? R.color.color_d2d2d2 : R.color.color_ffffff));
                        tvTopFollow.setText(isFollow ? "已关注" : "+关注");
                    }
                });
    }

}
