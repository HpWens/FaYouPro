package com.fy.fayou.event;

public class ListPraiseEvent {

    public int position;
    public String id;
    public boolean isPraise;

    public ListPraiseEvent(String id, int position, boolean isPraise) {
        this.position = position;
        this.id = id;
        this.isPraise = isPraise;
    }
}
