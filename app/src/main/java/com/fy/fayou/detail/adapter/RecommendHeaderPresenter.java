package com.fy.fayou.detail.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.detail.bean.RecommendHeaderBean;
import com.meis.base.mei.adapter.ItemPresenter;

public class RecommendHeaderPresenter extends ItemPresenter<RecommendHeaderBean> {
    @Override
    public int getLayoutRes() {
        return R.layout.item_article_recommend_header;
    }

    @Override
    public void convert(BaseViewHolder holder, RecommendHeaderBean item) {

    }
}
