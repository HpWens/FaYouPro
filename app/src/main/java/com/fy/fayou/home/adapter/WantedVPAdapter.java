package com.fy.fayou.home.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fy.fayou.contract.TemplateFragment;
import com.fy.fayou.home.fragment.WantedFragment;
import com.fy.fayou.legal.LegalFragment;
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
    // 法律法规
    public static int LEGAL = 3;
    // 司法解释
    public static int JUDICIAL = 4;
    // 指导性意见
    public static int GUIDE = 5;
    // 裁判文书
    public static int JUDGE = 6;

    // 收藏类型
    public int mCollectType;

    /***
     * @param fm
     * @param columns
     * @param type
     * @param collectType
     */
    public WantedVPAdapter(FragmentManager fm, ArrayList<ColumnEntity> columns, int type, int collectType) {
        super(fm);
        this.mColumns = columns;
        this.mType = type;
        this.mCollectType = collectType;
    }

    @Override
    public Fragment getItem(final int i) {
        if (mType == WANTED) {
            return WantedFragment.newInstance(mColumns.get(i).id, mColumns.get(i).name, mColumns.get(i).position);
        } else if (mType == TEMPLATE) {
            return TemplateFragment.newInstance(mColumns.get(i).type, mColumns.get(i).tags, mCollectType);
        } else if (mType == LEGAL || mType == JUDICIAL || mType == GUIDE || mType == JUDGE) {
            String id = mColumns.get(i).type;
            if (mType == JUDGE || mType == GUIDE) {
                id = mColumns.get(i).id;
            }
            return LegalFragment.newInstance(mType, id, mCollectType,
                    mType == JUDGE ? mColumns.get(i).judgeEntity : null,
                    mType == GUIDE ? mColumns.get(i).type : "");
        }
        return WantedFragment.newInstance(mColumns.get(i).type, "", "");
    }


    @Override
    public long getItemId(int position) {
        return mColumns.get(position).hashCode();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mColumns.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mType == LEGAL || mType == TEMPLATE || mType == JUDICIAL || mType == GUIDE || mType == JUDGE) {
            return mColumns.get(position).name + "(" + mColumns.get(position).number + ")";
        }
        return mColumns.get(position).type + "(" + mColumns.get(position).numberOfPeople + ")";
    }

    public void setNewData(ArrayList<ColumnEntity> columns) {
        this.mColumns = columns;
        notifyDataSetChanged();
    }
}