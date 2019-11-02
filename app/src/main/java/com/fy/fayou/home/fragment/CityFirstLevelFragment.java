package com.fy.fayou.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.bean.City;
import com.fy.fayou.common.CityService;
import com.fy.fayou.home.SelectCityActivity;
import com.fy.fayou.home.adapter.CityFirstLevelAdapter;
import com.meis.base.mei.base.BaseFragment;

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
                    mAdapter.setNewData(cities);
                    mAdapter.setItemChecked(0);
                    if (cities.get(0) != null) {
                        updateSecondLevelData(cities.get(0).children);
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
