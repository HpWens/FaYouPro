package com.fy.fayou.forum.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.forum.adapter.MenuAdapter;
import com.fy.fayou.search.bean.ColumnEntity;
import com.meis.base.mei.base.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MenuFragment extends BaseFragment {

    private static final String ARG_MENUS = "arg_menus";
    private static final String SAVE_STATE_POSITION = "save_state_position";

    @BindView(R.id.recycler)
    RecyclerView recycler;

    MenuAdapter adapter;
    Unbinder unbinder;

    private ArrayList<ColumnEntity> mMenus;
    private int mCurrentPosition = -1;

    public static MenuFragment newInstance(ArrayList<ColumnEntity> columns) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_MENUS, columns);

        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_transparent_recycler;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_STATE_POSITION, mCurrentPosition);
    }

    @Override
    protected void initView() {
        Bundle args = getArguments();
        if (args != null) {
            mMenus = args.getParcelableArrayList(ARG_MENUS);
        }
        unbinder = ButterKnife.bind(this, getView());

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter = new MenuAdapter());

        adapter.setNewData(mMenus);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
