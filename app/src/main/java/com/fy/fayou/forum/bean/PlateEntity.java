package com.fy.fayou.forum.bean;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.fy.fayou.bean.UserInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PlateEntity implements Serializable, Parcelable {

    public String id;
    public String name;
    public String logo;
    public int follows;
    public int comments;
    public boolean followed;
    public String description;
    public String parentId;
    public boolean deleted;
    public String nickName;
    public String avatar;
    public int posts;
    public String userId;
    public List<UserInfo> starUserList;
    public List<ForumEntity> indexPostList;

    // 辅助字段
    public boolean helperIsMy;

    public PlateEntity() {
    }

    protected PlateEntity(Parcel in) {
        id = in.readString();
        name = in.readString();
        logo = in.readString();
        follows = in.readInt();
        comments = in.readInt();
        followed = in.readByte() != 0;
        helperIsMy = in.readByte() != 0;
    }

    public static final Creator<PlateEntity> CREATOR = new Creator<PlateEntity>() {
        @Override
        public PlateEntity createFromParcel(Parcel in) {
            return new PlateEntity(in);
        }

        @Override
        public PlateEntity[] newArray(int size) {
            return new PlateEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(logo);
        dest.writeInt(follows);
        dest.writeInt(comments);
        dest.writeByte((byte) (followed ? 1 : 0));
        dest.writeByte((byte) (helperIsMy ? 1 : 0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlateEntity entity = (PlateEntity) o;
        return id.equals(entity.id) &&
                userId.equals(entity.userId);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
