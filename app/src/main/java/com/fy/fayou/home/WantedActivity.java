package com.fy.fayou.home;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.home.adapter.WantedVPAdapter;
import com.fy.fayou.search.bean.ColumnEntity;
import com.fy.fayou.utils.ParseUtils;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/home/wanted")
public class WantedActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;

    WantedVPAdapter mAdapter;

    private static final int CITY_REQUEST_CODE = 101;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.translucentStatusBar(this, true, false);
        tvRight.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        // 请求栏目
        requestColumn("");
    }

    private void requestColumn(String position) {
        EasyHttp.get(ApiUrl.CRIMINAL_COLUMN)
                .params("position", position)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            ArrayList<ColumnEntity> columns = ParseUtils.parseArrayListData(s, ColumnEntity.class);
                            if (mAdapter == null) {
                                viewpager.setAdapter(mAdapter = new WantedVPAdapter(getSupportFragmentManager(), columns, WantedVPAdapter.WANTED));
                                tab.setViewPager(viewpager);
                            } else {
                                mAdapter.setNewData(columns);
                                tab.setViewPager(viewpager);
                            }
                        }
                    }
                });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_home_wanted;
    }

    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                ARouter.getInstance().build(Constant.CITY_SELECT).navigation(mContext, CITY_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.Param.RESULT_CODE) {
            String city = data.getStringExtra(Constant.Param.CITY_NAME);
            tvRight.setText(city);
            requestColumn(city);
        }
    }
}
