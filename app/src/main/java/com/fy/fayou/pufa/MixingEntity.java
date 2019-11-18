package com.fy.fayou.pufa;

import java.io.Serializable;

public abstract class MixingEntity implements Serializable {

    public int type = getType();

    abstract int getType();
}
