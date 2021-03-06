package com.fy.fayou.detail.bean;

import java.io.Serializable;
import java.util.List;

public class ArticleEntity implements Serializable {

    public List<UserBean> giveRecords;

    public List<RecommendBean> recommendArticles;

    public List<CommentBean> commentList;

    public String id;
    public String userId;
    public String categoryName;
    public String fullTitle;
    public String description;
    public String cover;
    public String source;
    public String createTime;
    public String content;
    public int clicks;
    public int gives;
    public boolean give;
    public boolean follow;
    public String author;
    public String authorAvatar;
    public String auditName;
    public String title;
    public String auditId;
    public String articleType;
    public String videoUrl;
    public String auditAvatar;
    public List<String> tagNames;
    public String shareUrl;

    public String creatorName;
    public String creatorAvatar;
    public String disclaimer;
    public String creatorId;

    public static class UserBean {
        public String userId;
        public String userAvatar;
        public String nickName;
    }
}
