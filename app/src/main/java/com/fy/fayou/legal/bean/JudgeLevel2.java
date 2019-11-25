package com.fy.fayou.legal.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class JudgeLevel2 extends AbstractExpandableItem implements MultiItemEntity {

    public String id = "";
    public String parent;
    public String name = "";
    public boolean hasChald;

    // 辅助字段
    public boolean isSelected = false;
    public int level = 2;
    public int itemType = 2;
    public String helperId = "";
    public int helperIndex = 0;

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
