package com.fy.fayou.my;

import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.bean.CategoryEntity;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.my.adapter.NewsListVPAdapter;
import com.fy.fayou.utils.ParseUtils;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.status.ViewState;
import com.meis.base.mei.utils.Eyes;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/my/collect")
public class CollectActivity extends BaseActivity {

    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;

    NewsListVPAdapter mAdapter;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("我的收藏");
        setLeftBackListener(v -> finish());
    }

    @Override
    protected void initData() {
        setState(ViewState.EMPTY);
        // 请求栏目分类
        requestCategoryTag();
    }

    private void requestCategoryTag() {
        EasyHttp.get(ApiUrl.GET_COLLECT_CATEGORY)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            List<CategoryEntity> list = ParseUtils.parseListData(s, CategoryEntity.class);
                            viewpager.setAdapter(mAdapter = new NewsListVPAdapter(getSupportFragmentManager(), list, NewsListVPAdapter.COLLECT_TYPE));
                            tab.setViewPager(viewpager);
                            if (!list.isEmpty()) setState(ViewState.COMPLETED);
                        }
                    }
                });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_my_news_list;
    }
}
