package com.fy.fayou.detail.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.detail.bean.TextBean;
import com.meis.base.mei.adapter.ItemPresenter;

public class TextPresenter extends ItemPresenter<TextBean> {
    @Override
    public int getLayoutRes() {
        return R.layout.item_article_text;
    }

    @Override
    public void convert(BaseViewHolder holder, TextBean item) {

    }
}
