package com.fy.fayou.search.result;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.search.SearchResultActivity;
import com.fy.fayou.search.adapter.MenuListAdapter;
import com.meis.base.mei.base.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MenuListFragment extends BaseFragment {
    private static final String ARG_MENUS = "arg_menus";
    private static final String SAVE_STATE_POSITION = "save_state_position";

    @BindView(R.id.recycler)
    RecyclerView recycler;

    MenuListAdapter adapter;
    Unbinder unbinder;

    private ArrayList<String> mMenus;
    private int mCurrentPosition = -1;

    public static MenuListFragment newInstance(ArrayList<String> menus) {

        Bundle args = new Bundle();
        args.putStringArrayList(ARG_MENUS, menus);

        MenuListFragment fragment = new MenuListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_result_list;
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
            mMenus = args.getStringArrayList(ARG_MENUS);
        }
        unbinder = ButterKnife.bind(this, getView());

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter = new MenuListAdapter(position -> {
            if (position == mCurrentPosition) {
                return;
            }
            mCurrentPosition = position;
            adapter.setItemChecked(position);

            if (getActivity() instanceof SearchResultActivity) {
                ((SearchResultActivity) getActivity()).switchContentFragment(mMenus.get(position));
            }
        }));

        adapter.setNewData(mMenus);
        adapter.setItemChecked(0);
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
