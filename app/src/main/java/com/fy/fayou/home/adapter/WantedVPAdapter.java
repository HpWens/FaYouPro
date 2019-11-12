package com.fy.fayou.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.contract.TemplateFragment;
import com.fy.fayou.home.fragment.WantedFragment;
import com.fy.fayou.search.bean.ColumnEntity;

import java.util.ArrayList;

public class WantedVPAdapter extends FragmentPagerAdapter {

    private ArrayList<ColumnEntity> mColumns;

    /***
     * WANTED 全网通缉
     * TEMPLATE 合同模板
     */
    private int mType;
    // 合同模板
    public static int TEMPLATE = 2;
    // 全网通缉
    public static int WANTED = 1;

    public WantedVPAdapter(FragmentManager fm, ArrayList<ColumnEntity> columns, int type) {
        super(fm);
        this.mColumns = columns;
        this.mType = type;
    }

    @Override
    public Fragment getItem(final int i) {
        if (mType == WANTED) {
            return WantedFragment.newInstance(mColumns.get(i).type, "", "");
        } else if (mType == TEMPLATE) {
            return TemplateFragment.newInstance();
        }
        return WantedFragment.newInstance(mColumns.get(i).type, "", "");
    }

    @Override
    public int getCount() {
        return mColumns.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mColumns.get(position).type + "(" + mColumns.get(position).numberOfPeople + ")";
    }
}