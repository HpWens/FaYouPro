package com.fy.fayou.contract.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fy.fayou.contract.adapter.ExpandableItemAdapter;

public class Level1Item implements MultiItemEntity {

    public String content;
    public boolean isSelect;

    // 辅助布局
    public boolean isLast;

    public Level1Item() {
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_1;
    }
}
