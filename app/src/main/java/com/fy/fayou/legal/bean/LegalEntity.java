package com.fy.fayou.legal.bean;

import com.fy.fayou.bean.TagEntity;

import java.util.List;

public class LegalEntity {

    public String id;
    public String name;
    public String publishTime;
    public TagEntity type;
    public String updateUser;
    public String source;
    public int status;
    public String longText;
    public String judicialInterpretations;
    public String toUrl;
    public String createTime;
    public String updateTime;
    public List<TagEntity> tags;
}
