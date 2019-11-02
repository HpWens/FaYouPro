package com.fy.fayou.pufa.adapter;

import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.Constant;
import com.fy.fayou.pufa.bean.NewsEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class NewsAdapter extends MeiBaseAdapter<NewsEntity> {

    public NewsAdapter() {
        super(R.layout.item_article_recommend, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewsEntity item) {
        helper.itemView.setOnClickListener(v -> {
            ARouter.getInstance().build(Constant.DETAIL_ARTICLE).navigation();
        });
    }
}
