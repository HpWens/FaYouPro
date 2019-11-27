package com.fy.fayou.contract.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class Level0Item extends AbstractExpandableItem<Level1Item> implements MultiItemEntity {

    public String title;
    public int childCount;
    public int selectedNum;

    public String id;
    public String templeteId;
    public String content;
    public String parentId;
    public int level;


    public Level0Item() {
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public int getLevel() {
        return level;
    }
}
