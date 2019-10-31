package com.fy.fayou.detail.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.detail.bean.FooterBean;
import com.meis.base.mei.adapter.ItemPresenter;

public class FooterPresenter extends ItemPresenter<FooterBean> {
    @Override
    public int getLayoutRes() {
        return R.layout.item_article_footer;
    }

    @Override
    public void convert(BaseViewHolder holder, FooterBean item) {

    }
}
