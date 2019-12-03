package com.fy.fayou.forum.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ForumEntity implements MultiItemEntity {

    public String id;
    public String userId;
    public String author;
    public String authorAvatar;
    public String title;
    public String cover;
    public String description;
    public String createTime;
    public int clicks;
    public int gives;
    public int comments;
    public String boardName;
    public boolean given;

    @Override
    public int getItemType() {
        return 0;
    }
}
