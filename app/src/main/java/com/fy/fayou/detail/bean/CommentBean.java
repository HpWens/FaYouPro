package com.fy.fayou.detail.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommentBean implements Serializable, MultiItemEntity {

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
    public String userId;
    public List<CommentBean> childrenList = new ArrayList<>();

    // 辅助字段
    public int childIndex = 0;
    public boolean lastIndex = false;
    // 剩余评论的数量
    public int laveCommentCount;
    // 用于分级 0 父  1 子  2 展开其他评论
    public int level = 0;
    // 用于请求更多评论的辅助id
    public String helperId;
    public int helperChildCount;
    // 辅助页码
    public int helperPage;
    public int helperExpandNumber;

    @Override
    public int getItemType() {
        return level;
    }
}
