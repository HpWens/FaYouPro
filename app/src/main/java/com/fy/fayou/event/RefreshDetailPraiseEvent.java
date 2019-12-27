package com.fy.fayou.event;

public class RefreshDetailPraiseEvent {

    public String articleId;
    public String commentId;
    public boolean isForum;
    public boolean isPraise;

    public RefreshDetailPraiseEvent(String articleId, String commentId, boolean isForum, boolean isPraise) {
        this.articleId = articleId;
        this.commentId = commentId;
        this.isForum = isForum;
        this.isPraise = isPraise;
    }
}
