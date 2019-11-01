package com.fy.fayou.detail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fy.fayou.R;
import com.fy.fayou.view.CustomPopWindow;
import com.meis.base.mei.adapter.BaseMultiAdapter;

import java.util.ArrayList;

public class ReviewAdapter extends BaseMultiAdapter<MultiItemEntity> {

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    public ReviewAdapter() {
        super(new ArrayList<>());
        addItemType(TYPE_LEVEL_0, R.layout.item_comment_level0);
        addItemType(TYPE_LEVEL_1, R.layout.item_comment_level1);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiItemEntity item) {
        helper.getView(R.id.tv_content).setOnLongClickListener(v -> {
                    showPop(mContext, v);
                    return false;
                }
        );
    }

    private void showPop(Context context, View view) {
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(context)
                .setView(R.layout.pop_review_report)
                .create();
        popWindow.showAsDropDown(view);
    }
}
