package com.fy.fayou.detail.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.detail.bean.HeaderBean;
import com.fy.fayou.utils.GlideOption;
import com.meis.base.mei.adapter.ItemPresenter;
import com.vondear.rxtool.RxImageTool;

public class HeaderPresenter extends ItemPresenter<HeaderBean> {
    @Override
    public int getLayoutRes() {
        return R.layout.item_article_header;
    }

    @Override
    public void convert(BaseViewHolder holder, HeaderBean item) {
        holder.setText(R.id.tv_title, getNonEmpty(item.fullTitle))
                .setText(R.id.tv_name, getNonEmpty(item.auditName))
                .setText(R.id.tv_time, "发布于" + getNonEmpty(item.createTime));

        holder.getView(R.id.tv_follow).setSelected(item.follow);

        ImageView ivAvatar = holder.getView(R.id.iv_avatar);
        Glide.with(holder.itemView.getContext())
                .load(getNonEmpty(item.auditAvatar))
                .apply(GlideOption.getAvatarOption(RxImageTool.dp2px(35), RxImageTool.dp2px(35)))
                .into(ivAvatar);
    }
}
