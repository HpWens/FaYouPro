package com.fy.fayou.common;

import java.util.List;

public class ApiResult<T> {

    public int count;
    public int page;
    public int size;
    public int pages;
    public List<T> content;

}
