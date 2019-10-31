package com.fy.fayou.detail.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.detail.bean.CommentBean;
import com.meis.base.mei.adapter.ItemPresenter;

public class CommentPresenter extends ItemPresenter<CommentBean> {
    @Override
    public int getLayoutRes() {
        return R.layout.item_article_comment;
    }

    @Override
    public void convert(BaseViewHolder holder, CommentBean item) {

    }
}
