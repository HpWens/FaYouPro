package com.fy.fayou.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class RecommendEntity implements Serializable, MultiItemEntity {

    public static final int TYPE_TOP = 0X0001;
    public static final int TYPE_ARTICLE = 0X0002;
    public static final int TYPE_VIDEO = 0X0003;

    public boolean give;
    public int gives;
    public String id;
    public String categoryName;
    public String fullTitle;
    public String description;
    public String cover;
    public String source;
    public String createTime;
    public String author;
    public String articleType;
    // 是否置顶
    public boolean showIndex;
    public String videoUrl;

    public int fixedMode = 1;

    @Override
    public int getItemType() {
        if (showIndex) return TYPE_TOP;
        if (!TextUtils.isEmpty(articleType)) {
            if (articleType.equals("VIDEO")) {
                return TYPE_VIDEO;
            }
            return TYPE_ARTICLE;
        }
        return TYPE_ARTICLE;
    }
}
