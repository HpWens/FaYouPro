package com.fy.fayou.detail.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.detail.bean.PicBean;
import com.fy.fayou.utils.TransformWrapHeightUtils;
import com.meis.base.mei.adapter.ItemPresenter;

public class PicPresenter extends ItemPresenter<PicBean> {

    private OnClickListener mListener;

    public PicPresenter(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_article_pic;
    }

    @Override
    public void convert(BaseViewHolder holder, PicBean item) {

        ImageView ivPic = holder.getView(R.id.iv_pic);

        Glide.with(holder.itemView.getContext())
                .asBitmap()
                .load(getNonEmpty(item.httpPath))
                .into(new TransformWrapHeightUtils(ivPic));

        holder.itemView.setOnClickListener(v -> {
            mListener.onClick(v, item);
        });

    }

    public interface OnClickListener {
        void onClick(View v, PicBean item);
    }
}
