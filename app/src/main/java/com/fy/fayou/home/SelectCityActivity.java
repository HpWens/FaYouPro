package com.fy.fayou.home;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.fy.fayou.bean.City;
import com.fy.fayou.home.fragment.CityFirstLevelFragment;
import com.fy.fayou.home.fragment.CitySecondLevelFragment;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import java.util.List;

import butterknife.ButterKnife;

@Route(path = "/city/select")
public class SelectCityActivity extends BaseActivity {

    CitySecondLevelFragment secondLevelFragment;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);

        setToolBarCenterTitle("选择城市");
        setLeftBackListener(v -> finish());
    }

    @Override
    protected void initData() {

        if (findFragment(CityFirstLevelFragment.class) == null) {

            CityFirstLevelFragment menuListFragment = CityFirstLevelFragment.newInstance();
            loadRootFragment(R.id.fl_list_container, menuListFragment);
            // false:  不加入回退栈;  false: 不显示动画
            loadRootFragment(R.id.fl_content_container, secondLevelFragment = CitySecondLevelFragment.newInstance(), false, false);
        }
    }

    /**
     * @param cities
     */
    public void updateSecondLevelData(List<City> cities) {
        if (secondLevelFragment != null) {
            secondLevelFragment.setNewData(cities);
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_city_select;
    }
}
