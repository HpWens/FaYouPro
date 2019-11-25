package com.fy.fayou.legal.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class JudgeLevel1 extends AbstractExpandableItem<JudgeLevel2> implements MultiItemEntity, Parcelable {

    public String id;
    public String parent;
    public String name = "";
    public boolean hasChald;

    // 辅助字段
    public int helperIndex;

    protected JudgeLevel1(Parcel in) {
        id = in.readString();
        parent = in.readString();
        name = in.readString();
        hasChald = in.readByte() != 0;
    }

    public static final Creator<JudgeLevel1> CREATOR = new Creator<JudgeLevel1>() {
        @Override
        public JudgeLevel1 createFromParcel(Parcel in) {
            return new JudgeLevel1(in);
        }

        @Override
        public JudgeLevel1[] newArray(int size) {
            return new JudgeLevel1[size];
        }
    };

    @Override
    public int getItemType() {
        return 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(parent);
        dest.writeString(name);
        dest.writeByte((byte) (hasChald ? 1 : 0));
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
