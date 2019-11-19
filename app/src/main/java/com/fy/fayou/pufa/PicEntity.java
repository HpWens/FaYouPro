package com.fy.fayou.pufa;

import java.io.Serializable;

/**
 * Created by wenshi on 2019/7/3.
 * Description
 */
public class PicEntity implements Serializable {

    public int position;

    public String path = "";

    public long duration = 0;

    public boolean isSelected;

    public String originPath = "";

    public String httpPath;

    // 辅助字段
    public int type = 2;
}
