package com.fy.fayou.home.adapter;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.home.bean.WantedEntity;
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
        String content = tvContent.getText().toString();

        tvContent.post(() -> {
            float dWidth = tvDetail.getWidth();
            float width = tvContent.getPaint().measureText(content);
            int lineWidth = tvContent.getWidth();

            if ((width + dWidth) >= 4 * lineWidth) {
                int endIndex = (int) ((4 * lineWidth - dWidth) / width * content.length());
                tvContent.setText(content.substring(0, endIndex - 4) + "...");
            }
        });

    }
}
