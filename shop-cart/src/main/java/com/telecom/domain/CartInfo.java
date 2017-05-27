package com.telecom.domain;

/**
 * Created by Administrator on 2017/5/26.
 */
public class CartInfo {
    private long id;
    private long item_id;
    private long num;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    @Override
    public String toString() {
        return "CartInfo{" +
                "id=" + id +
                ", item_id=" + item_id +
                ", num=" + num +
                '}';
    }
}
