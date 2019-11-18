package com.fy.fayou.pufa;

import java.io.Serializable;

public class TitleEntity extends MixingEntity implements Serializable {

    public String name;

    @Override
    int getType() {
        return 0;
    }
}
