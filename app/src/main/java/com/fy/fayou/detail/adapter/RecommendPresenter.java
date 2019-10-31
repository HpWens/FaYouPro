package com.fy.fayou.detail.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.detail.bean.RecommendBean;
import com.meis.base.mei.adapter.ItemPresenter;

public class RecommendPresenter extends ItemPresenter<RecommendBean> {
    @Override
    public int getLayoutRes() {
        return R.layout.item_article_recommend;
    }

    @Override
    public void convert(BaseViewHolder holder, RecommendBean item) {

    }
}
