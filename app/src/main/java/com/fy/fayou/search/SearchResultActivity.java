package com.fy.fayou.search;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.fy.fayou.search.result.ContentFragment;
import com.fy.fayou.search.result.MenuListFragment;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.base.BaseFragment;
import com.meis.base.mei.utils.Eyes;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportHelper;

@Route(path = "/home/result_search")
public class SearchResultActivity extends BaseActivity {
    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);

    }

    @Override
    protected void initData() {

        if (findFragment(MenuListFragment.class) == null) {
            ArrayList<String> listMenus = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.array_menu)));
            MenuListFragment menuListFragment = MenuListFragment.newInstance(listMenus);
            loadRootFragment(R.id.fl_list_container, menuListFragment);
            // false:  不加入回退栈;  false: 不显示动画
            loadRootFragment(R.id.fl_content_container, ContentFragment.newInstance(listMenus.get(0)), false, false);
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_search_result;
    }

    public void switchContentFragment(String menu) {
        ContentFragment fragment = ContentFragment.newInstance(menu);
        BaseFragment baseFragment = SupportHelper.findFragment(getSupportFragmentManager(), ContentFragment.class);
        if (baseFragment != null) {
            baseFragment.replaceFragment(fragment, false);
        }
    }

}
