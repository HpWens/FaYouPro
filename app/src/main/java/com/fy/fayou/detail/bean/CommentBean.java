package com.fy.fayou.detail.bean;

import java.io.Serializable;

public class CommentBean implements Serializable {
    public String id;
    public String createTime;
    public String updateTime;
    public String version;
    public String articleId;
    public String parentId;
    public int gives;
    public int comments;
    public String content;
    public String reUserId;
    public String reUserName;
    public String reUserAvatar;
    public boolean deleted;
    public String userName;
    public String articleTitle;
    public String userAvatar;
    public boolean give;

    // 辅助字段
    public int childIndex = 0;
    public boolean lastIndex = false;
}
