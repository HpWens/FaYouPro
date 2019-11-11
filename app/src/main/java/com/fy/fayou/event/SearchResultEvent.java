package com.fy.fayou.event;

public class SearchResultEvent {

    public static final int RESULT_CLEAN = 0x111;
    public static final int RESULT_FINISH = 0x222;
    public static final int RESULT_FOCUS = 0x333;

    public int code;

    public SearchResultEvent(int code) {
        this.code = code;
    }
}
