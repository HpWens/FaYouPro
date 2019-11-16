package com.fy.fayou.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class LaunchEntity implements MultiItemEntity {

    public int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }
}
