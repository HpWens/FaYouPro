package com.fy.fayou.common;

import java.io.Serializable;
import java.util.List;

public class ApiResult<T> implements Serializable {

    public int count;
    public int page;
    public int size;
    public int pages;
    public List<T> content;

}
