package com.fy.fayou.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.bean.City;
import com.fy.fayou.common.CityService;
import com.fy.fayou.common.UserService;
import com.fy.fayou.home.SelectCityActivity;
import com.fy.fayou.home.adapter.CityFirstLevelAdapter;
import com.fy.fayou.utils.MapLocationUtils;
import com.meis.base.mei.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CityFirstLevelFragment extends BaseFragment {

    @BindView(R.id.recycler)
    RecyclerView recycler;

    Unbinder unbinder;

    CityFirstLevelAdapter mAdapter;

    private int mCurrentPosition = -1;

    public static CityFirstLevelFragment newInstance() {
        Bundle args = new Bundle();
        CityFirstLevelFragment fragment = new CityFirstLevelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, getView());
    }

    @Override
    protected void initData() {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(mAdapter = new CityFirstLevelAdapter());

        mAdapter.setOnItemClickListener((position, data) -> {
            if (position == mCurrentPosition) {
                return;
            }
            mCurrentPosition = position;
            mAdapter.setItemChecked(position);

            updateSecondLevelData(data);
        });

        loadCityData();
    }

    private void loadCityData() {
        CityService.getInstance().loadCityData(getActivity().getApplicationContext())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cities -> {
                    City firstCity = new City();
                    firstCity.name = "定位/全国";
                    // 添加定位 历史选择数据
                    addLocationAndHistory(firstCity);
                    cities.add(0, firstCity);
                    mAdapter.setNewData(cities);
                    mAdapter.setItemChecked(0);
                    if (cities.get(0) != null) {
                        updateSecondLevelData(cities.get(0).children);
                    }

                    startLocation();
                });
    }

    // 添加定位 历史选择数据
    private void addLocationAndHistory(City firstCity) {
        firstCity.children = new ArrayList<>();
        City city = new City();
        city.name = "定位";
        city.spanSize = City.FULL_ROW;
        firstCity.children.add(city);

        city = new City();
        city.name = "全国";
        city.isHistory = true;
        city.spanSize = City.NORMAL_ROW;
        city.childrenIndex = 0;
        firstCity.children.add(city);

        // 获取到历史记录
        List<String> histories = UserService.getInstance().getClassify(UserService.CLASSIFY_CITY_HISTORY);
        if (histories != null && !histories.isEmpty()) {
            city = new City();
            city.name = "历史选择";
            city.spanSize = City.FULL_ROW;
            firstCity.children.add(city);
            for (int i = 0; i < histories.size(); i++) {
                city = new City();
                city.name = histories.get(i);
                city.spanSize = City.NORMAL_ROW;
                city.isHistory = true;
                city.childrenIndex = i;
                firstCity.children.add(city);
            }
        }
    }

    private void startLocation() {
        MapLocationUtils.startLocation(getActivity(), cityName -> {
            City city = new City();
            city.isSelect = true;
            city.name = cityName;
            city.isHistory = true;
            city.spanSize = City.NORMAL_ROW;
            city.childrenIndex = 0;

            List<City> list = mAdapter.getData();
            if (list != null && !list.isEmpty()) {
                // 获取到定位/全国数据
                List<City> firstChildren = list.get(0).children;
                if (firstChildren != null && firstChildren.size() > 1) {
                    firstChildren.get(1).childrenIndex = 1;
                    firstChildren.add(1, city);
                    // 更新数据
                    updateSecondLevelData(firstChildren);
                }
            }

        });
    }

    public void updateSecondLevelData(List<City> cities) {
        if (getActivity() instanceof SelectCityActivity) {
            ((SelectCityActivity) getActivity()).updateSecondLevelData(cities);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_transparent_recycler;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
