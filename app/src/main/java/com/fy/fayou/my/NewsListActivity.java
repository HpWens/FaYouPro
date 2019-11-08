package com.fy.fayou.my;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.my.adapter.NewsListVPAdapter;
import com.fy.fayou.my.fragment.NewsListFragment;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/news/list")
public class NewsListActivity extends BaseActivity {

    private final String[] mTitles = {
            "全部", "待审核", "审核通过", "审核未通过"
    };

    private final String[] mTypes = {
            NewsListFragment.ALL_STATUS,
            NewsListFragment.ALL_SUBMIT,
            NewsListFragment.ALL_AUDIT,
            NewsListFragment.ALL_FAIL
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
        setToolBarCenterTitle("我的资讯");
        setLeftBackListener(v -> finish())
                .setRightMoreRes(R.mipmap.ic_publish_black)
                .setRightMoreListener(v -> {

                });
    }

    @Override
    protected void initData() {
        requestStatus();

        viewpager.setAdapter(mAdapter = new NewsListVPAdapter(getSupportFragmentManager(), mTitles, NewsListVPAdapter.NEWS_TYPE, mTypes));
        tab.setViewPager(viewpager);
    }

    private void requestStatus() {
        EasyHttp.get(ApiUrl.MY_NEWS_STATUS)
                .baseUrl(Constant.BASE_URL4)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {

                    }
                });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_my_news_list;
    }
}
