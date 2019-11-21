package com.fy.fayou.my.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.pufa.bean.NewsEntity;
import com.fy.fayou.utils.GlideOption;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class NewsListAdapter extends MeiBaseAdapter<NewsEntity> {

    public NewsListAdapter() {
        super(R.layout.item_my_news, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewsEntity item) {
        helper.setText(R.id.tv_title, getNonEmpty(item.fullTitle));

        TextView tvStatus = helper.getView(R.id.tv_status);
        ImageView ivStatus = helper.getView(R.id.iv_status);
        switch (item.auditStatus) {
            case "SUBMIT":
                tvStatus.setText("待审核");
                tvStatus.setTextColor(helper.itemView.getResources().getColor(R.color.color_fe8a4d));
                ivStatus.setImageResource(R.mipmap.ic_news_verifying);
                break;
            case "AUDIT":
                tvStatus.setText("审核成功");
                tvStatus.setTextColor(helper.itemView.getResources().getColor(R.color.color_33be33));
                ivStatus.setImageResource(R.mipmap.ic_news_verifed);
                break;
            case "AUDIT_FAIL":
                tvStatus.setText("审核未通过");
                tvStatus.setTextColor(helper.itemView.getResources().getColor(R.color.color_a0a0a0));
                ivStatus.setImageResource(R.mipmap.ic_news_failure);
                break;
        }

        helper.setGone(R.id.line_view, !TextUtils.isEmpty(item.auditRemark))
                .setGone(R.id.tv_season, !TextUtils.isEmpty(item.auditRemark))
                .setText(R.id.tv_season, getNonEmpty(item.auditRemark));

        ImageView ivCover = helper.getView(R.id.iv_cover);

        if (TextUtils.isEmpty(item.cover)) {
            ivCover.setVisibility(View.GONE);
        } else {
            ivCover.setVisibility(View.VISIBLE);

            Glide.with(mContext)
                    .load(item.cover)
                    .apply(GlideOption.getRadiusOption(112, 74, 2))
                    .into(ivCover);
        }
    }
}
