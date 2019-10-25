package com.fy.fayou.common;

// 处理recycler与viewpager产生的上下滑动冲突
public interface OnScrollClashListener {

    void onScroll(boolean isScroll);

}
