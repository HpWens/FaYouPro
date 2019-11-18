package com.fy.fayou.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.adapter.HomePuFaVPAdapter;
import com.fy.fayou.bean.CategoryEntity;
import com.fy.fayou.bean.TagEntity;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
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

                            viewpager.setAdapter(mAdapter = new HomePuFaVPAdapter(getChildFragmentManager(), list));
                            tab.setViewPager(viewpager);
                        }
                    }
                });
    }

    @Override
    protected void initData() {

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

    @OnClick(R.id.iv_publish)
    public void onClick() {
        ARouter.getInstance().build(Constant.NEWS_PUBLISH).navigation();
    }
}
