package com.fy.fayou.detail.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.detail.bean.CommentHeaderBean;
import com.meis.base.mei.adapter.ItemPresenter;

public class CommentHeaderPresenter extends ItemPresenter<CommentHeaderBean> {
    @Override
    public int getLayoutRes() {
        return R.layout.item_article_comment_header;
    }

    @Override
    public void convert(BaseViewHolder holder, CommentHeaderBean item) {

    }
}
