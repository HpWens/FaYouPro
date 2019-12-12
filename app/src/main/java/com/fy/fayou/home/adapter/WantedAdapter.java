package com.fy.fayou.home.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.home.bean.WantedEntity;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.KtTimeUtils;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class WantedAdapter extends MeiBaseAdapter<WantedEntity> {

    public WantedAdapter() {
        super(R.layout.item_wanted, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WantedEntity item) {

        TextView tvDetail = helper.getView(R.id.tv_detail);
        TextView tvContent = helper.getView(R.id.tv_content);
        String content = getNonEmpty(item.content.replaceAll("　", ""));

        tvContent.post(() -> {
            float dWidth = tvDetail.getWidth();
            float width = tvContent.getPaint().measureText(content);
            int lineWidth = tvContent.getWidth();

            if ((width + dWidth) >= 4 * lineWidth) {
                int endIndex = (int) ((4 * lineWidth - dWidth) / width * content.length());
                tvContent.setText(content.substring(0, endIndex - 8) + "...");
            } else {
                tvContent.setText(content);
            }
        });

        helper.setText(R.id.tv_name, getNonEmpty(item.name))
                .setText(R.id.tv_time, "发布时间：" + KtTimeUtils.INSTANCE.getYMDTime(item.pubTime));
        helper.setVisible(R.id.tv_time, !TextUtils.isEmpty(item.pubTime));

        ImageView ivThumb = helper.getView(R.id.iv_thumb);

        Glide.with(mContext)
                .load(getNonEmpty(item.photoUrl))
                .apply(GlideOption.getItemOption(75, 92))
                .into(ivThumb);

        helper.itemView.setOnClickListener(v -> {
            try {
                String id = item.toUrl.substring(item.toUrl.lastIndexOf("/") + 1);
                ARoute.jumpH5(item.toUrl, false, id, ARoute.WANTED_TYPE);
            } catch (Exception e) {
            }
        });

    }
}
