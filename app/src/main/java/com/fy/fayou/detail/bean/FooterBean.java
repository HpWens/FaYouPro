package com.fy.fayou.detail.bean;

import java.util.ArrayList;
import java.util.List;

public class FooterBean {

    public String author;
    public String source;
    public List<String> tagNames;
    public int gives;
    public boolean give;
    public String id;
    public String disclaimer;
    public int type;

    public List<ArticleEntity.UserBean> giveRecords = new ArrayList<>();

}
