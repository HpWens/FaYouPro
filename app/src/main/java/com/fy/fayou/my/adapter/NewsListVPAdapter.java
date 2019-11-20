package com.fy.fayou.my.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.bean.CategoryEntity;
import com.fy.fayou.my.fragment.CollectFragment;
import com.fy.fayou.my.fragment.NewsListFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsListVPAdapter extends FragmentPagerAdapter {

    private List<CategoryEntity> mCategoryList = new ArrayList<>();

    private String[] mTitles;

    private String[] mTypes;

    private int type = 1;

    public static final int COLLECT_TYPE = 2;

    public static final int NEWS_TYPE = 1;

    public static final int HISTORY_TYPE = 3;

    public NewsListVPAdapter(FragmentManager fm, String[] titles, int type, String[] types) {
        super(fm);
        this.mTitles = titles;
        this.mTypes = types;
        this.type = type;
    }

    public NewsListVPAdapter(FragmentManager fm, List<CategoryEntity> data, int type) {
        super(fm);
        this.mCategoryList = data;
        this.type = type;
    }

    @Override
    public Fragment getItem(int i) {
        if (type == NEWS_TYPE) {
            return NewsListFragment.newInstance(mTypes[i]);
        } else if (type == COLLECT_TYPE) {
            return CollectFragment.newInstance(true, mCategoryList.get(i).type);
        } else if (type == HISTORY_TYPE) {
            return CollectFragment.newInstance(false, mCategoryList.get(i).type);
        }
        return NewsListFragment.newInstance("");
    }

    @Override
    public int getCount() {
        if (type == COLLECT_TYPE || type == HISTORY_TYPE) {
            return mCategoryList.size();
        }
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (type == COLLECT_TYPE || type == HISTORY_TYPE) {
            return mCategoryList.get(position).title;
        }
        return mTitles[position];
    }
}
