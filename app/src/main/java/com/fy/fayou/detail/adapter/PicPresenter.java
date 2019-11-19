package com.fy.fayou.detail.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.detail.bean.PicBean;
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
                .override(Target.SIZE_ORIGINAL)
                .into(ivPic);

    }
}
