package com.major.base.rx.rxbus;

/**
 * TODO
 * Created by MEI on 2017/8/2.
 */

public class RxEvent {

    public int what;
    public Object obj1;
    public Object obj2;

    public RxEvent() {
    }

    public RxEvent(int what) {
        this.what = what;
    }

    public RxEvent(int what, Object obj1) {
        this.what = what;
        this.obj1 = obj1;
    }

    public RxEvent(int what, Object obj1, Object obj2) {
        this.what = what;
        this.obj1 = obj1;
        this.obj2 = obj2;
    }
}
