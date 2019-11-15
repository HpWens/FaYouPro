package com.fy.fayou.legal;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
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

@Route(path = "/home/legal")
public class LegalActivity extends BaseActivity {

    @Autowired(name = "type")
    public int moduleType = ARoute.LEGAL_TYPE;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_center_title)
    TextView tvCenterTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;
    @BindView(R.id.iv_float_search)
    ImageView ivFloatSearch;

    WantedVPAdapter mAdapter;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.translucentStatusBar(this, true, false);
    }

    @Override
    protected void initData() {
        switch (moduleType) {
            case ARoute.LEGAL_TYPE:
                tvCenterTitle.setText("法律法规");
                break;
            case ARoute.JUDICIAL_TYPE:
                tvCenterTitle.setText("司法解释");
                break;
            case ARoute.GUIDE_TYPE:
                tvCenterTitle.setText("指导性意见");
                break;
            case ARoute.JUDGE_TYPE:
                tvCenterTitle.setText("裁判文书");
                tvRight.setText("筛选");
                tvRight.setVisibility(View.VISIBLE);
                break;
            case ARoute.BOOKS_TYPE:
                tvCenterTitle.setText("法律图书");
                break;
            default:
                break;
        }
        // 请求栏目1法律法规2司法解释
        requestColumn(moduleType);
    }

    private void requestColumn(int type) {
        EasyHttp.get(ApiUrl.LEGAL_FIND_LIST)
                .params("type", type + "")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            ArrayList<ColumnEntity> columns = ParseUtils.parseArrayListData(s, ColumnEntity.class);
                            viewpager.setAdapter(mAdapter = new WantedVPAdapter(getSupportFragmentManager(), columns, WantedVPAdapter.LEGAL));
                            tab.setViewPager(viewpager);
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
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_float_search:

                break;
        }
    }


}
