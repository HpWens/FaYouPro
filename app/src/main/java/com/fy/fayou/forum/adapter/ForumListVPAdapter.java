package com.fy.fayou.forum.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.common.OnScrollClashListener;
import com.fy.fayou.forum.bean.ForumEntity;
import com.fy.fayou.forum.fragment.ForumListFragment;

import java.util.ArrayList;
import java.util.List;

public class ForumListVPAdapter extends FragmentPagerAdapter {

    private String[] mTitles;

    private OnScrollClashListener mListener;

    private ArrayList<ForumEntity> mTopList = new ArrayList<>();

    // 板块ID
    private String mId;

    public ForumListVPAdapter(FragmentManager fm, String id, String[] titles, List<ForumEntity> topList) {
        super(fm);
        this.mTitles = titles;
        this.mTopList.addAll(topList);
        this.mId = id;
    }

    @Override
    public Fragment getItem(int i) {
        return ForumListFragment.newInstance(i, mId, mTopList).setOnScrollClashListener(isScroll -> {
            if (mListener != null) mListener.onScroll(isScroll);
        });
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }


    public void setOnScrollClashListener(OnScrollClashListener listener) {
        mListener = listener;
    }
}
