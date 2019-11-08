package com.fy.fayou.search;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.search.bean.MenuEntity;
import com.fy.fayou.search.result.ContentFragment;
import com.fy.fayou.search.result.MenuListFragment;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.base.BaseFragment;
import com.meis.base.mei.utils.Eyes;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportHelper;

@Route(path = "/home/result_search")
public class SearchResultActivity extends BaseActivity {

    @Autowired
    public String keyword = "";

    @BindView(R.id.et_search)
    TextView etSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.iv_close)
    ImageView ivClose;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
    }

    @Override
    protected void initData() {

        etSearch.setText(keyword);
        tvCancel.setOnClickListener(v -> {
            finish();
        });

        ivClose.setOnClickListener(v -> {
            tvCancel.performClick();
        });

        requestMenuListData();
    }

    private void requestMenuListData() {
        EasyHttp.get(ApiUrl.SEARCH_RESULT_MENU)
                .params("keyword", keyword)
                .baseUrl(Constant.BASE_URL6)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            MenuEntity entity = ParseUtils.parseData(s, MenuEntity.class);

                            ArrayList<String> listData = new ArrayList<>();
                            if (entity.legalProvisions != null) {
                                listData.add("法律法规\n(" + entity.legalProvisions + ")");
                            }
                            if (entity.judicialInterpretation != null) {
                                listData.add("司法解释\n(" + entity.judicialInterpretation + ")");
                            }
                            if (entity.newInfo != null) {
                                listData.add("新闻\n(" + entity.newInfo + ")");
                            }
                            if (entity.judgement != null) {
                                listData.add("判决文书\n(" + entity.judgement + ")");
                            }
                            if (entity.caseInfo != null) {
                                listData.add("案例\n(" + entity.caseInfo + ")");
                            }

                            if (findFragment(MenuListFragment.class) == null) {
                                ArrayList<String> listMenus = listData;
                                MenuListFragment menuListFragment = MenuListFragment.newInstance(listMenus);
                                loadRootFragment(R.id.fl_list_container, menuListFragment);
                                // false:  不加入回退栈;  false: 不显示动画
                                loadRootFragment(R.id.fl_content_container, ContentFragment.newInstance(listMenus.get(0), keyword), false, false);
                            }

                        }
                    }
                });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_search_result;
    }

    public void switchContentFragment(String menu) {
        ContentFragment fragment = ContentFragment.newInstance(menu, keyword);
        BaseFragment baseFragment = SupportHelper.findFragment(getSupportFragmentManager(), ContentFragment.class);
        if (baseFragment != null) {
            baseFragment.replaceFragment(fragment, false);
        }
    }
}
