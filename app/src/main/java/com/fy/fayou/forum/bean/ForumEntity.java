package com.fy.fayou.forum.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ForumEntity implements MultiItemEntity, Parcelable {

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

    // 辅助字段 是否置顶
    public boolean helperIsTop;
    public int itemType;

    protected ForumEntity(Parcel in) {
        id = in.readString();
        userId = in.readString();
        author = in.readString();
        authorAvatar = in.readString();
        title = in.readString();
        cover = in.readString();
        description = in.readString();
        createTime = in.readString();
        clicks = in.readInt();
        gives = in.readInt();
        comments = in.readInt();
        boardName = in.readString();
        given = in.readByte() != 0;
    }

    public static final Creator<ForumEntity> CREATOR = new Creator<ForumEntity>() {
        @Override
        public ForumEntity createFromParcel(Parcel in) {
            return new ForumEntity(in);
        }

        @Override
        public ForumEntity[] newArray(int size) {
            return new ForumEntity[size];
        }
    };

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(author);
        dest.writeString(authorAvatar);
        dest.writeString(title);
        dest.writeString(cover);
        dest.writeString(description);
        dest.writeString(createTime);
        dest.writeInt(clicks);
        dest.writeInt(gives);
        dest.writeInt(comments);
        dest.writeString(boardName);
        dest.writeByte((byte) (given ? 1 : 0));
    }
}
