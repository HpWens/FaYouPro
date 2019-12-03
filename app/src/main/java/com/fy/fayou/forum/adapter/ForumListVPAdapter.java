package com.fy.fayou.forum.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.common.OnScrollClashListener;
import com.fy.fayou.forum.fragment.ForumListFragment;

public class ForumListVPAdapter extends FragmentPagerAdapter {

    private String[] mTitles;

    private OnScrollClashListener mListener;


    public ForumListVPAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        return ForumListFragment.newInstance().setOnScrollClashListener(isScroll -> {
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
