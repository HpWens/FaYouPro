package com.fy.fayou.event;

import com.fy.fayou.detail.bean.CommentBean;

public class RefreshDetailCommentEvent {

    public CommentBean entity;
    public String detailId = "";
    public boolean isParent;

    public RefreshDetailCommentEvent(CommentBean entity, String detailId, boolean isParent) {
        this.entity = entity;
        this.detailId = detailId;
        this.isParent = isParent;
    }
}
