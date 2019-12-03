package com.fy.fayou.forum.bean;

import java.io.Serializable;

public class PlateEntity implements Serializable {

    public String id;
    public String name;
    public String logo;
    public int follows;
    public int comments;
    public boolean followed;

    // 辅助字段
    public boolean helperIsMy;

}
