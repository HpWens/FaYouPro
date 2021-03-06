package com.fy.fayou.detail.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.Constant;
import com.fy.fayou.detail.bean.FooterBean;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.view.flow.FlowLayout;
import com.fy.fayou.view.radius.RadiusTextView;
import com.meis.base.mei.adapter.ItemPresenter;
import com.vondear.rxtool.RxImageTool;

import de.hdodenhof.circleimageview.CircleImageView;

public class FooterPresenter extends ItemPresenter<FooterBean> {

    OnClickListener mListener;

    public FooterPresenter(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_article_footer;
    }

    @Override
    public void convert(BaseViewHolder holder, FooterBean item) {
        holder.setText(R.id.tv_origin, "来源：" + getNonEmpty(item.source))
                .setText(R.id.tv_author, "原作者：" + getNonEmpty(item.author))
                .setText(R.id.tv_praise_num, item.gives + "人点赞")
                .setGone(R.id.source_layout, item.type != Constant.Param.FORUM_TYPE)
                .setGone(R.id.flow_tag, item.type != Constant.Param.FORUM_TYPE)
                .setGone(R.id.tv_origin, !TextUtils.isEmpty(item.source))
                .setGone(R.id.source_layout, item.type == Constant.Param.FORUM_TYPE ? false : !TextUtils.isEmpty(item.source + item.author))
                .setGone(R.id.tv_author, !TextUtils.isEmpty(item.author))
                .setGone(R.id.tv_affirm, !TextUtils.isEmpty(item.disclaimer))
                .setText(R.id.tv_affirm, item.disclaimer);


        LinearLayout priseLayout = holder.getView(R.id.praise_layout);

        if (!isEmpty(item.giveRecords) && item.type != Constant.Param.FORUM_TYPE) {
            priseLayout.setVisibility(View.VISIBLE);
            CircleImageView firstIv = holder.getView(R.id.iv_avatar_first);
            CircleImageView secondIv = holder.getView(R.id.iv_avatar_second);
            CircleImageView threeIv = holder.getView(R.id.iv_avatar_three);
            holder.setGone(R.id.iv_avatar_first, true)
                    .setGone(R.id.iv_avatar_second, true)
                    .setGone(R.id.iv_avatar_three, true);
            int size = item.giveRecords.size();

            for (int i = 0; i < (size > 3 ? 3 : size); i++) {
                Glide.with(holder.itemView.getContext())
                        .load(getNonEmpty(item.giveRecords.get(i).userAvatar))
                        .apply(GlideOption.getAvatarOption(RxImageTool.dp2px(20), RxImageTool.dp2px(20)))
                        .into(i == 0 ? firstIv : (i == 1 ? secondIv : threeIv));
            }

            if (size == 2) {
                threeIv.setVisibility(View.GONE);
            } else if (size == 1) {
                secondIv.setVisibility(View.GONE);
                threeIv.setVisibility(View.GONE);
            }

        } else {
            priseLayout.setVisibility(View.GONE);

            if (item.type == Constant.Param.FORUM_TYPE && item.gives > 0) {
                priseLayout.getChildAt(0).setVisibility(View.GONE);
                priseLayout.getChildAt(1).setVisibility(View.GONE);
                priseLayout.getChildAt(2).setVisibility(View.GONE);
                priseLayout.setVisibility(View.VISIBLE);
            }
        }

        FlowLayout tagLayout = holder.getView(R.id.flow_tag);
        if (!isEmpty(item.tagNames)) {
            tagLayout.removeAllViews();
            for (String tag : item.tagNames) {
                View tagView = View.inflate(holder.itemView.getContext(), R.layout.item_detail_tag, null);
                RadiusTextView tvTag = tagView.findViewById(R.id.tv_tag);
                tvTag.setOnClickListener(v -> {
                    ARoute.jumpSearchResult(tag, item.type == Constant.Param.FORUM_TYPE);
                });
                tvTag.setText(tag);
                tagLayout.addView(tagView);
            }
        }

        ImageView ivPraise = holder.getView(R.id.iv_praise);
        ivPraise.setSelected(item.give);
        ivPraise.setOnClickListener(v -> {
            mListener.onClick(v, item.id, holder.getAdapterPosition(), item);
        });
    }

    public interface OnClickListener {
        void onClick(View v, String id, int position, FooterBean footer);
    }

}
