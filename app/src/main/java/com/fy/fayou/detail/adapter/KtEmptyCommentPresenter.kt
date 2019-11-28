package com.fy.fayou.detail.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.fy.fayou.R
import com.fy.fayou.detail.bean.EmptyCommentBean
import com.meis.base.mei.adapter.ItemPresenter

class KtEmptyCommentPresenter : ItemPresenter<EmptyCommentBean>() {

    override fun convert(holder: BaseViewHolder?, item: EmptyCommentBean?) {

    }

    override fun getLayoutRes() = R.layout.item_article_empty_comment

}