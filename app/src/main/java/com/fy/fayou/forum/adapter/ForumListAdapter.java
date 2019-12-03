package com.fy.fayou.forum.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.forum.bean.ForumEntity;
import com.meis.base.mei.adapter.BaseMultiAdapter;

import java.util.List;

public class ForumListAdapter extends BaseMultiAdapter<ForumEntity> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ForumListAdapter(List<ForumEntity> data) {
        super(data);
        addItemType(0, R.layout.item_forum_list_top);
        addItemType(1, R.layout.item_forum_list_content);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ForumEntity item) {
        super.convert(helper, item);

    }
}
