package com.fy.fayou.pufa;

import java.io.Serializable;

/**
 * Created by wenshi on 2019/7/3.
 * Description
 */
public class TextEntity implements Serializable {

    public int position;

    public String content = "";

    public String hint = "";

    public boolean isFocused;

    // 辅助字段
    public int type = 1;
}
