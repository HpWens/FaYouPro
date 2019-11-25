package com.fy.fayou.contract.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fy.fayou.contract.adapter.ExpandableItemAdapter;

public class Level1Item implements MultiItemEntity, Parcelable {

    public String id;
    public String parent;
    public String name = "";
    public boolean hasChald;

    public String content;
    public boolean isSelect;

    // 辅助布局
    public boolean isLast;

    public Level1Item() {
    }

    protected Level1Item(Parcel in) {
        id = in.readString();
        parent = in.readString();
        name = in.readString();
        hasChald = in.readByte() != 0;
        content = in.readString();
        isSelect = in.readByte() != 0;
        isLast = in.readByte() != 0;
    }

    public static final Creator<Level1Item> CREATOR = new Creator<Level1Item>() {
        @Override
        public Level1Item createFromParcel(Parcel in) {
            return new Level1Item(in);
        }

        @Override
        public Level1Item[] newArray(int size) {
            return new Level1Item[size];
        }
    };

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_1;
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
        dest.writeString(content);
        dest.writeByte((byte) (isSelect ? 1 : 0));
        dest.writeByte((byte) (isLast ? 1 : 0));
    }
}
