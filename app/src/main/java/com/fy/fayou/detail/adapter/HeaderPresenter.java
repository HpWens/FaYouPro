package com.fy.fayou.detail.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.detail.bean.HeaderBean;
import com.meis.base.mei.adapter.ItemPresenter;

public class HeaderPresenter extends ItemPresenter<HeaderBean> {
    @Override
    public int getLayoutRes() {
        return R.layout.item_article_header;
    }

    @Override
    public void convert(BaseViewHolder holder, HeaderBean item) {

    }
}
