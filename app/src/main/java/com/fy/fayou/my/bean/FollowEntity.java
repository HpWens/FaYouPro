package com.fy.fayou.my.bean;

import com.fy.fayou.bean.UserInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FollowEntity implements Serializable {

    public List<UserInfo> content = new ArrayList<>();

    public int page;

    public int size;

    public int pages;

    public int counts;

}
