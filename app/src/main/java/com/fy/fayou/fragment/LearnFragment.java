package com.fy.fayou.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.adapter.HomePuFaVPAdapter;
import com.fy.fayou.bean.CategoryEntity;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.UserService;
import com.fy.fayou.utils.ParseUtils;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseFragment;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LearnFragment extends BaseFragment {

    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.iv_publish)
    ImageView ivPublish;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;

    Unbinder unbinder;
    int selectedPosition = -1;
    private HomePuFaVPAdapter mAdapter;

    public static LearnFragment newInstance() {
        Bundle args = new Bundle();
        LearnFragment fragment = new LearnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, getView());

        // 请求栏目分类
        requestCategoryTag();
    }

    private void requestCategoryTag() {
        EasyHttp.get(ApiUrl.FIND_CATEGORY)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            List<CategoryEntity> list = ParseUtils.parseListData(s, CategoryEntity.class);
                            if (list.isEmpty()) return;
                            selectedPosition = 0;
                            ivPublish.setVisibility(list.get(selectedPosition).enableUserAdd ? View.VISIBLE : View.GONE);

                            viewpager.setAdapter(mAdapter = new HomePuFaVPAdapter(getChildFragmentManager(), list));
                            viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int i, float v, int i1) {
                                }

                                @Override
                                public void onPageSelected(int i) {
                                    selectedPosition = i;
                                    ivPublish.setVisibility(list.get(i).enableUserAdd ? View.VISIBLE : View.GONE);
                                }

                                @Override
                                public void onPageScrollStateChanged(int i) {
                                }
                            });
                            tab.setViewPager(viewpager);
                        }
                    }
                });
    }

    @Override
    protected void initData() {

    }

    public void switchTab(int pos) {
        if (tab != null && tab.getTabCount() > pos && tab.getCurrentTab() != pos) {
            tab.setCurrentTab(pos);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.learn_fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_publish, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_publish:
                if (!UserService.getInstance().checkLoginAndJump()) return;

                if (selectedPosition == -1 || mAdapter == null) {
                    return;
                }

                ARoute.jumpPublishNews(mAdapter.getTags().get(selectedPosition).id + "");

                break;
            case R.id.iv_search:
                ARoute.jumpSearch();
                break;
        }

    }
}
