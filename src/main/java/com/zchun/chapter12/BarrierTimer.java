package com.zchun.chapter12;

/**
 * @author chun.zhou
 * @date 2018/7/28 16:52
 */
public class BarrierTimer implements Runnable {

    private boolean started;
    private long startTime, endTime;

    @Override
    public synchronized void run() {
        long t = System.nanoTime();
        if (!started) {
            started = true;
            startTime = t;
        } else {
            endTime = t;
        }
    }

    public synchronized void clear() {
        started = false;
    }

    public synchronized long getTime() {
        return endTime - startTime;
    }
}

