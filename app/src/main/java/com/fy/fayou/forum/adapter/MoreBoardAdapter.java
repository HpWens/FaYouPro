package com.fy.fayou.forum.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.forum.bean.PlateEntity;
import com.fy.fayou.utils.GlideOption;
import com.meis.base.mei.adapter.MeiBaseAdapter;

public class MoreBoardAdapter extends MeiBaseAdapter<PlateEntity> {

    private String keyword;

    public MoreBoardAdapter(String keyword) {
        super(R.layout.item_search_board);
        this.keyword = keyword;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PlateEntity item) {

        ImageView ivLogo = helper.getView(R.id.iv_thumb);
        Glide.with(mContext)
                .load(getNonEmpty(item.logo))
                .apply(GlideOption.getRadiusOption(50, 50, 2))
                .into(ivLogo);

        helper.setText(R.id.tv_name, getForeSpan(keyword, getNonEmpty(item.name)))
                .setText(R.id.tv_desc, getForeSpan(keyword, getNonEmpty(item.description)));

        helper.itemView.setOnClickListener(v -> {
            ARoute.jumpPlateList(item.id);
        });
    }

    /**
     * @param span
     * @param content
     * @return
     */
    public SpannableString getForeSpan(String span, String content) {
        if (!TextUtils.isEmpty(content)) {
            content = content.replaceAll("\n", "");
        } else {
            content = "";
        }
        SpannableString spannableString = new SpannableString(content);
        if (content.contains(span)) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ED4040"));
            spannableString.setSpan(colorSpan, content.indexOf(span), content.indexOf(span) + span.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return spannableString;
    }
}
