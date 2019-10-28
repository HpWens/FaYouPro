package com.fy.fayou.search;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.fy.fayou.search.result.ContentFragment;
import com.fy.fayou.search.result.MenuListFragment;
import com.meis.base.mei.base.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;

@Route(path = "/home/result_search")
public class SearchResultActivity extends BaseActivity {
    @Override
    protected void initView() {

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

}
