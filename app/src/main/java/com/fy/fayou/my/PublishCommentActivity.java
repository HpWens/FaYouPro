package com.fy.fayou.my;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.my.adapter.PublishCommentVPAdapter;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/my/publish_comment")
public class PublishCommentActivity extends BaseActivity {

    String[] titles = {"发表", "评论"};
    PublishCommentVPAdapter adapter;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.iv_search)
    ImageView ivSearch;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);

    }

    @Override
    protected void initData() {
        viewPager.setAdapter(adapter = new PublishCommentVPAdapter(getSupportFragmentManager(), titles));
        tab.setViewPager(viewPager);
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_publish_comment;
    }

    @OnClick({R.id.iv_back, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                break;
        }
    }

}
