package com.fy.fayou.contract;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.bean.TagEntity;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.contract.dialog.TagDialog;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/contract/template")
public class TemplateActivity extends BaseActivity {

    @Autowired(name = "type")
    public int collectType = ARoute.TEMPLATE_TYPE;

    @BindView(R.id.tv_center_title)
    TextView tvCenterTitle;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;
    WantedVPAdapter mAdapter;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_float_search)
    ImageView ivFloatSearch;

    List<TagEntity> mTagListData = new ArrayList<>();

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.translucentStatusBar(this, true, false);

        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("筛选");
        tvCenterTitle.setText("合同模板");
    }

    @Override
    protected void initData() {
        requestColumn("");
    }

    private void requestColumn(String tags) {
        EasyHttp.get(ApiUrl.GET_TEMPLATE_TYPE)
                .params("tags", tags)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            ArrayList<ColumnEntity> columns = ParseUtils.parseArrayListData(s, ColumnEntity.class);
                            for (ColumnEntity column : columns) {
                                column.tags = tags;
                            }
                            if (mAdapter == null) {
                                viewpager.setAdapter(mAdapter = new WantedVPAdapter(getSupportFragmentManager(), columns, WantedVPAdapter.TEMPLATE, collectType));
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

    @OnClick({R.id.iv_back, R.id.tv_right, R.id.iv_float_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_float_search:
                ARoute.jumpSearch();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                TagDialog dialog = new TagDialog(this, (v, data, dia) -> {
                    mTagListData = data;
                    if (!data.isEmpty()) {
                        tvRight.setText(data.get(0).name);
                        requestColumn(data.get(0).id);
                    } else {
                        requestColumn("");
                    }
                    dia.dismiss();
                }, mTagListData);
                dialog.show();
                break;
        }
    }
}
