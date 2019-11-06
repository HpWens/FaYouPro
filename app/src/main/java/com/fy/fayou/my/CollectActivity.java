package com.fy.fayou.my;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.my.adapter.NewsListVPAdapter;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/my/collect")
public class CollectActivity extends BaseActivity {

    private final String[] mTitles = {
            "全部", "普法", "帖子", "法律法规", "司法解释", "指导性意见"
    };

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
        viewpager.setAdapter(mAdapter = new NewsListVPAdapter(getSupportFragmentManager(), mTitles, NewsListVPAdapter.COLLECT_TYPE));
        tab.setViewPager(viewpager);
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_my_news_list;
    }
}