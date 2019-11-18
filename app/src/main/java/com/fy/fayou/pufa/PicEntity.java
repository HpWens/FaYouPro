package com.fy.fayou.pufa;

import java.io.Serializable;

/**
 * Created by wenshi on 2019/7/3.
 * Description
 */
public class PicEntity extends MixingEntity implements Serializable {

    public int position;

    public String path = "";

    public long duration = 0;

    public boolean isSelected;

    public String originPath = "";

    @Override
    int getType() {
        return 2;
    }
}
