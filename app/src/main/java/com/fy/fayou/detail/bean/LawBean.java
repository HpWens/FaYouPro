package com.fy.fayou.detail.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class LawBean implements Serializable, MultiItemEntity {

    public String id;
    public String name;
    public String url;
    public int collectType;
    public int itemType = 0;

    @Override
    public int getItemType() {
        return itemType;
    }
}
