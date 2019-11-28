package com.fy.fayou.search.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

public class SearchResultEntity implements Serializable, MultiItemEntity {

    // 旧版
    public String name;
    public String title;
    public String content;
    public String result;
    public String author;

    // 新版
    public String jid;
    public String pubTime;
    public String source;
    public String createTime;
    public String type;
    public String lables;
    public String toUrl;
    public int itemType = 0;
    public String logo;
    public int columnType;
    public int headerIndex = 0;

    // 合同模板
    public String contractId;
    public String id;
    public String termsNumber;
    public int isFree;
    public String expiryDate;
    public String price;
    public String download;

    // 法律法规
    public String lawId;
    public String publishTime;

    // 指导性意见
    public String originalEditor;
    public String publicDate;

    // 热门视频小视频
    public String newsInfoId;
    public String cover;
    public String articleType;
    public String videoUrl;
    public String categoryId;
    public int orderNum;
    public String upTime;
    public List<SearchResultEntity> videoList;

    public int childIndex;
    public boolean isLastChild;

    @Override
    public int getItemType() {
        return itemType;
    }


}
