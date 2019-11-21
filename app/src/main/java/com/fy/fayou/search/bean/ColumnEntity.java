package com.fy.fayou.search.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class ColumnEntity implements Parcelable, Serializable {

    // 栏目名称
    public String name;

    // 栏目副标题
    public String subName;

    // 用于请求接口参数
    public String param;

    // 法律法规数量
    public String number;

    // 全网通缉
    public String type;
    // 全网通缉数量
    public String numberOfPeople;


    public ColumnEntity() {
    }


    protected ColumnEntity(Parcel in) {
        name = in.readString();
        subName = in.readString();
        param = in.readString();
        type = in.readString();
        numberOfPeople = in.readString();
    }

    public static final Creator<ColumnEntity> CREATOR = new Creator<ColumnEntity>() {
        @Override
        public ColumnEntity createFromParcel(Parcel in) {
            return new ColumnEntity(in);
        }

        @Override
        public ColumnEntity[] newArray(int size) {
            return new ColumnEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(subName);
        dest.writeString(param);
        dest.writeString(type);
        dest.writeString(numberOfPeople);
    }
}
