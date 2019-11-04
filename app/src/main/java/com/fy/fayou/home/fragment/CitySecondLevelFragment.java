package com.fy.fayou.home.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fy.fayou.R;
import com.fy.fayou.bean.City;
import com.fy.fayou.home.adapter.CitySecondLevelAdapter;
import com.meis.base.mei.base.BaseFragment;
import com.vondear.rxtool.RxImageTool;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CitySecondLevelFragment extends BaseFragment {

    CitySecondLevelAdapter mAdapter;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    private int mSpacing;
    Unbinder unbinder;

    public static CitySecondLevelFragment newInstance() {
        Bundle args = new Bundle();
        CitySecondLevelFragment fragment = new CitySecondLevelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, getView());
    }

    @Override
    protected void initData() {
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler.setAdapter(mAdapter = new CitySecondLevelAdapter());
        mSpacing = RxImageTool.dip2px(15);

        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int pos = parent.getChildAdapterPosition(view);
                if (pos % 2 == 0) {
                    outRect.left = mSpacing;
                    outRect.right = mSpacing / 2;
                } else {
                    outRect.left = mSpacing / 2;
                    outRect.right = mSpacing;

                }
            }
        });
    }

    public void setNewData(List<City> cities) {
        if (mAdapter != null) mAdapter.setNewData(cities);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_recycler;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
