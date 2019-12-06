package com.fy.fayou.detail.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.detail.bean.CommentBean;
import com.fy.fayou.utils.GlideOption;
import com.meis.base.mei.adapter.ItemPresenter;
import com.vondear.rxtool.RxImageTool;

public class CommentPresenter extends ItemPresenter<CommentBean> {

    OnClickListener mListener;

    boolean isForum;

    public CommentPresenter(OnClickListener listener, boolean isForum) {
        mListener = listener;
        this.isForum = isForum;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_article_comment;
    }

    @Override
    public void convert(BaseViewHolder holder, CommentBean item) {
        holder.setText(R.id.tv_name, getNonEmpty(item.userName))
                .setText(R.id.tv_content, getNonEmpty(item.content))
                .setText(R.id.tv_praise_num, item.gives + "")
                .setGone(R.id.tv_look, item.lastIndex);

        holder.getView(R.id.iv_praise).setSelected(isForum ? item.given : item.give);

        ImageView ivAvatar = holder.getView(R.id.iv_avatar);
        Glide.with(holder.itemView.getContext())
                .load(getNonEmpty(item.userAvatar))
                .apply(GlideOption.getAvatarOption(RxImageTool.dp2px(30), RxImageTool.dp2px(30)))
                .into(ivAvatar);

        holder.getView(R.id.praise_layout).setOnClickListener(v -> {
            // 请求点赞
            mListener.onPraise(v, holder.getAdapterPosition(), item);
        });

        holder.getView(R.id.tv_look).setOnClickListener(v -> {
            mListener.onLook(v);
        });
    }

    public interface OnClickListener {

        void onPraise(View v, int position, CommentBean comment);

        void onLook(View view);
    }


}
