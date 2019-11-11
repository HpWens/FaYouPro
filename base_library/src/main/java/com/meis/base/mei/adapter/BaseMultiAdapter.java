package com.meis.base.mei.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.Collection;
import java.util.List;

public class BaseMultiAdapter<T extends MultiItemEntity> extends BaseMultiItemQuickAdapter<T, BaseViewHolder> {

    /**
     * 当前页码
     * current page
     */
    private int mPageCount;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BaseMultiAdapter(List<T> data) {
        super(data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, T item) {

    }


    @Override
    public void setNewData(@Nullable List<T> data) {
        super.setNewData(data);
        mPageCount = 1;
    }

    @Override
    public void addData(@NonNull Collection<? extends T> newData) {
        super.addData(newData);
        mPageCount++;
    }

    public void clearData() {
        getData().clear();
        notifyDataSetChanged();
        mPageCount = 0;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public int getDataCount() {
        return getData().size();
    }

    public String getNonEmpty(String ch) {
        return TextUtils.isEmpty(ch) ? "" : ch;
    }
}
