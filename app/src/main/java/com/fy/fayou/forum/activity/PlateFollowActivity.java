package com.fy.fayou.forum.activity;

import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.adapter.HomeForumVPAdapter;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.event.RefreshFollowPlateEvent;
import com.fy.fayou.forum.bean.PlateEntity;
import com.fy.fayou.utils.ParseUtils;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


@Route(path = "/plate/follow")
public class PlateFollowActivity extends BaseActivity {

    HomeForumVPAdapter mAdapter;

    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setLeftBackListener(v -> {
            // 刷新关注板块
            EventBus.getDefault().post(new RefreshFollowPlateEvent());
            finish();
        });
    }

    @Override
    protected void initData() {

        EasyHttp.get(ApiUrl.FORUM_MY_FOLLOW)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            List<PlateEntity> list = ParseUtils.parseListData(s, "content", PlateEntity.class);

                            viewpager.setAdapter(mAdapter = new HomeForumVPAdapter(getSupportFragmentManager(), list, 1));
                            tab.setViewPager(viewpager);
                        }
                    }
                });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_forum_plate;
    }

    @Override
    public void onBackPressedSupport() {
        // 刷新关注板块
        EventBus.getDefault().post(new RefreshFollowPlateEvent());
        super.onBackPressedSupport();
    }
}
