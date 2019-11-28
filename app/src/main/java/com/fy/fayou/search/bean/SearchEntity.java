package com.fy.fayou.search.bean;

import java.io.Serializable;
import java.util.List;

public class SearchEntity implements Serializable {

    // 旧版
    public String name;
    public String id;
    public String createTime;
    public String updateTime;
    public String version;
    public String keyword = "";
    public String searchNumber;
    public String recordTime;

    // 新版
    public int type;
    public String logo;
    public List<SearchResultEntity> data;

}
