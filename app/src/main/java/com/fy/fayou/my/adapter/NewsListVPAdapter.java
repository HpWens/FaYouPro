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
            return NewsListFragment.newInstance(mCategoryList.get(i).auditStatus);
        } else if (type == COLLECT_TYPE) {
            return CollectFragment.newInstance(true, mCategoryList.get(i).type);
        } else if (type == HISTORY_TYPE) {
            return CollectFragment.newInstance(false, mCategoryList.get(i).type);
        }
        return NewsListFragment.newInstance("");
    }

    @Override
    public int getCount() {
        if (type == COLLECT_TYPE || type == HISTORY_TYPE || type == NEWS_TYPE) {
            return mCategoryList.size();
        }
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (type == COLLECT_TYPE || type == HISTORY_TYPE) {
            return mCategoryList.get(position).title;
        } else if (type == NEWS_TYPE) {
            String status = mCategoryList.get(position).auditStatus;
            int num = mCategoryList.get(position).num;
            // 审核状态    SUBMIT待审核,     AUDIT审核通过,     AUDIT_FAIL审核未通过
            switch (status) {
                case "ALL":
                    return "全部";
                case "SUBMIT":
                    return "待审核(" + num + ")";
                case "AUDIT":
                    return "审核通过(" + num + ")";
                case "AUDIT_FAIL":
                    return "审核未通过(" + num + ")";
                default:
                    return "";
            }
        }
        return mTitles[position];
    }
}
