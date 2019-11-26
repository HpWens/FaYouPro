package com.fy.fayou.legal;

import android.content.Intent;
import android.support.annotation.Nullable;
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
import com.fy.fayou.common.Constant;
import com.fy.fayou.home.adapter.WantedVPAdapter;
import com.fy.fayou.legal.bean.JudgeEntity;
import com.fy.fayou.legal.bean.JudgeLevel1;
import com.fy.fayou.legal.bean.JudgeLevel2;
import com.fy.fayou.search.bean.ColumnEntity;
import com.fy.fayou.utils.ParseUtils;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.status.ViewState;
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
                //tvRight.setText("全国");
                //tvRight.setVisibility(View.VISIBLE);
                break;
            case ARoute.JUDGE_TYPE:
                tvCenterTitle.setText("裁判文书");
                tvRight.setText("筛选");
                tvRight.setVisibility(View.VISIBLE);
                break;
            case ARoute.BOOKS_TYPE:
                tvCenterTitle.setText("法律图书");
                setState(ViewState.EMPTY);
                return;
            default:
                break;
        }
        // 请求栏目1法律法规2司法解释3指导性意见
        if (moduleType == ARoute.GUIDE_TYPE) {
            requestGuideColumn();
        } else if (moduleType == ARoute.JUDGE_TYPE) {
            requestJudgeCategory();
        } else {
            requestColumn(moduleType);
        }
    }

    // 请求指导性栏目
    private void requestGuideColumn() {
        EasyHttp.get(ApiUrl.GET_GUIDE_LIST)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            ArrayList<ColumnEntity> columns = ParseUtils.parseArrayListData(s, ColumnEntity.class);
                            viewpager.setAdapter(mAdapter = new WantedVPAdapter(getSupportFragmentManager(), columns, WantedVPAdapter.GUIDE, moduleType));
                            tab.setViewPager(viewpager);
                        }
                    }
                });
    }

    private void requestColumn(final int type) {
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
                            viewpager.setAdapter(mAdapter = new WantedVPAdapter(getSupportFragmentManager(), columns, type == ARoute.LEGAL_TYPE ? WantedVPAdapter.LEGAL : WantedVPAdapter.JUDICIAL, moduleType));
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
            case R.id.tv_right:
                ARoute.jumpJudgeFilter(this, filterList);
                break;
            case R.id.iv_float_search:
                ARoute.jumpSearch();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.Param.RESULT_CODE) {
            String city = data.getStringExtra(Constant.Param.CITY_NAME);
            tvRight.setText(city);
        } else if (resultCode == Constant.Param.TEMPLATE_FILTER_RESULT) {
            ArrayList<JudgeLevel2> list = data.getParcelableArrayListExtra(Constant.Param.LIST);
            if (list == null || list.isEmpty()) {
                requestJudgeCategory();
            } else {
                String[] ids = new String[]{"0", "0", "0", "0", "0", "0", parentId};
                for (JudgeLevel2 lv : list) {
                    if (ids.length > lv.helperIndex) {
                        ids[lv.helperIndex] = lv.id;
                    }
                }
                StringBuilder sb = new StringBuilder();
                JudgeEntity judgeEntity = new JudgeEntity();
                for (int i = 0; i < ids.length; i++) {
                    sb.append(ids[i] + ",");
                    setJudgeEntity(i, ids[i], judgeEntity);
                }
                requestFilterCategory(judgeEntity, sb.subSequence(0, sb.length() - 1).toString());
            }
        }
    }

    /**********************************************裁判文书接口**********************************************/
    private void setJudgeEntity(int index, String id, JudgeEntity judge) {
        switch (index) {
            case 0:
                judge.typeReason = id;
                break;
            case 1:
                judge.typeCourtClass = id;
                break;
            case 2:
                judge.typeZone = id;
                break;
            case 3:
                judge.typeSpnf = id;
                break;
            case 4:
                judge.typeSpcx = id;
                break;
            case 5:
                judge.typeBookType = id;
                break;

        }
    }

    private String parentId = "0";

    /**
     * @param judge
     * @param types
     */
    private void requestFilterCategory(JudgeEntity judge, String types) {
        EasyHttp.get(ApiUrl.GET_JUDGE_FILTER_CATEGORY)
                .params("types", types)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            ArrayList<ColumnEntity> columns = ParseUtils.parseArrayListData(s, ColumnEntity.class);
                            for (ColumnEntity column : columns) {
                                column.judgeEntity = judge;
                            }
                            if (mAdapter == null) {
                                viewpager.setAdapter(mAdapter = new WantedVPAdapter(getSupportFragmentManager(), columns, WantedVPAdapter.JUDGE, moduleType));
                                tab.setViewPager(viewpager);
                            } else {
                                mAdapter.setNewData(columns);
                                tab.setViewPager(viewpager);
                                tab.setCurrentTab(0);
                            }
                        }
                    }
                });
    }

    private ArrayList<JudgeLevel1> filterList = new ArrayList<>();

    private void requestJudgeCategory() {
        EasyHttp.get(ApiUrl.GET_JUDGE_LEVEL)
                .params("id", "0")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            filterList = ParseUtils.parseArrayListData(s, JudgeLevel1.class);
                            String id = "";
                            for (JudgeLevel1 level : filterList) {
                                if (!TextUtils.isEmpty(level.name) && level.name.equals(getString(R.string.judge_category_sign))) {
                                    parentId = id = level.id;
                                    break;
                                }
                            }

                            if (!TextUtils.isEmpty(id)) {
                                requestJudgeCategory(id);
                            }

                        }
                    }
                });
    }

    private void requestJudgeCategory(String id) {
        EasyHttp.get(ApiUrl.GET_JUDGE_CATEGORY)
                .params("id", "" + id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            ArrayList<ColumnEntity> columns = ParseUtils.parseArrayListData(s, ColumnEntity.class);
                            if (mAdapter == null) {
                                viewpager.setAdapter(mAdapter = new WantedVPAdapter(getSupportFragmentManager(), columns, WantedVPAdapter.JUDGE, moduleType));
                                tab.setViewPager(viewpager);
                            } else {
                                mAdapter.setNewData(columns);
                                tab.setViewPager(viewpager);
                            }
                        }
                    }
                });
    }

}
