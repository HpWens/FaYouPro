package com.fy.fayou.search.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.fy.fayou.legal.bean.JudgeEntity;

import java.io.Serializable;


public class ColumnEntity implements Serializable, Parcelable {

    // 栏目名称
    public String name = "";

    // 栏目副标题
    public String subName;

    // 用于请求接口参数
    public String param;

    // 法律法规数量
    public String number;

    // 全网通缉
    public String type = "";
    // 全网通缉数量
    public String numberOfPeople;

    public String position = "";

    public String count;
    public String id = "";
    // 辅助字段
    public JudgeEntity judgeEntity;
    public String tags;

    public String card_type;

    public ColumnEntity() {
    }

    protected ColumnEntity(Parcel in) {
        name = in.readString();
        subName = in.readString();
        param = in.readString();
        number = in.readString();
        type = in.readString();
        numberOfPeople = in.readString();
        count = in.readString();
        id = in.readString();
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
        dest.writeString(number);
        dest.writeString(type);
        dest.writeString(numberOfPeople);
        dest.writeString(count);
        dest.writeString(id);
    }
}
