package com.fy.fayou.legal.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class JudgeLevel2 extends AbstractExpandableItem implements MultiItemEntity, Parcelable {

    public String id = "";
    public String parent;
    public String name = "";
    public boolean hasChald;

    // 辅助字段 保存最顶级名称
    public String topLevelName;

    // 辅助字段
    public boolean isSelected = false;
    public int level = 2;
    public int itemType = 2;
    public String helperId = "";
    public int helperIndex = 0;
    public boolean isFirstChild;

    protected JudgeLevel2(Parcel in) {
        id = in.readString();
        parent = in.readString();
        name = in.readString();
        hasChald = in.readByte() != 0;
        topLevelName = in.readString();
        isSelected = in.readByte() != 0;
        level = in.readInt();
        itemType = in.readInt();
        helperId = in.readString();
        helperIndex = in.readInt();
        isFirstChild = in.readByte() != 0;
    }

    public static final Creator<JudgeLevel2> CREATOR = new Creator<JudgeLevel2>() {
        @Override
        public JudgeLevel2 createFromParcel(Parcel in) {
            return new JudgeLevel2(in);
        }

        @Override
        public JudgeLevel2[] newArray(int size) {
            return new JudgeLevel2[size];
        }
    };

    @Override
    public int getLevel() {
        return level;
    }

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
        dest.writeString(parent);
        dest.writeString(name);
        dest.writeByte((byte) (hasChald ? 1 : 0));
        dest.writeString(topLevelName);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeInt(level);
        dest.writeInt(itemType);
        dest.writeString(helperId);
        dest.writeInt(helperIndex);
        dest.writeByte((byte) (isFirstChild ? 1 : 0));
    }
}
