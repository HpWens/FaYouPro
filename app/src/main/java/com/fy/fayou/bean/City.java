/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fy.fayou.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * <p>区域实体。</p>
 * Created by ws on 2017/6/1.
 */
public class City implements Parcelable, MultiItemEntity {

    /**
     * id。
     */
    public String id;

    /**
     * 名称。
     */
    public String name;

    /**
     * 子项。
     */
    public List<City> children;

    /**
     * 是否选中。
     */
    public boolean isSelect;


    // 辅助字段
    public int spanSize = NORMAL_ROW;
    public int childrenIndex = 0;
    public boolean isHistory;
    public static final int FULL_ROW = 2;
    public static final int NORMAL_ROW = 1;

    public City() {
    }

    protected City(Parcel in) {
        id = in.readString();
        name = in.readString();
        children = in.createTypedArrayList(City.CREATOR);
        isSelect = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeTypedList(children);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String mId) {
        id = mId;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        name = mName;
    }

    public List<City> getCityList() {
        return children;
    }

    public void setCityList(List<City> mCityList) {
        this.children = mCityList;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean mSelect) {
        isSelect = mSelect;
    }

    public City(String mId, String mName, List<City> mCityList, boolean mIsSelect) {
        id = mId;
        name = mName;
        this.children = mCityList;
        isSelect = mIsSelect;
    }

    @Override
    public int getItemType() {
        return spanSize;
    }
}
