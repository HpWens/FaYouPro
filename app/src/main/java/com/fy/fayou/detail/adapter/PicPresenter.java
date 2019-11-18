package com.fy.fayou.detail.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.detail.bean.PicBean;
import com.fy.fayou.utils.GlideOption;
import com.meis.base.mei.adapter.ItemPresenter;

public class PicPresenter extends ItemPresenter<PicBean> {
    @Override
    public int getLayoutRes() {
        return R.layout.item_article_pic;
    }

    @Override
    public void convert(BaseViewHolder holder, PicBean item) {

        ImageView ivPic = holder.getView(R.id.iv_pic);
        Glide.with(holder.itemView.getContext())
                .load(getNonEmpty(item.httpPath))
                .apply(GlideOption.getFullScreenWOption(holder.itemView.getContext()))
                .into(ivPic);
    }
}
