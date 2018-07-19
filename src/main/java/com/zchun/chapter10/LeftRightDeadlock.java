package com.zchun.chapter10;

/**
 * 容易产生死锁
 * @author chun.zhou
 * @date 2018/7/19 9:45
 */
public class LeftRightDeadlock {
    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight() {
        synchronized (left) {
            synchronized(right) {
                doSomething();
            }
        }
    }

    public void rightLeft() {
        synchronized(right) {
            synchronized(left){
                doSomethingElse();
            }
        }
    }

    public void doSomething(){}
    public void doSomethingElse(){}
}
