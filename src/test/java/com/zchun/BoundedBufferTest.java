package com.zchun;

import com.zchun.chapter12.BoundedBuffer;

import junit.framework.TestCase;

/**
 * @author chun.zhou
 * @date 2018/7/27 9:40
 */
public class BoundedBufferTest extends TestCase {

    private static final Long LOCKUP_DETECT_TIMEOUT = 1000L;

    public void testIsEmptyWhenConstructed() {
        BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        assertTrue(bb.isEmpty());
        assertFalse(bb.isFull());
    }

    public void testIsFullAfterPuts() throws InterruptedException {
        BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        for (int i = 0; i < 10; i++) {
            bb.put(i);
        }
        assertTrue(bb.isFull());
        assertFalse(bb.isEmpty());
    }

    public void testTakeBlocksWhenEmpty() {
        final BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);
        Thread taker = new Thread(){
            @Override
            public void run() {
                try {
                    int unused = bb.take();
                    //如果运行到这来，说明有错误。
                    fail();
                } catch (InterruptedException e){};
            }
        };

        try {
            taker.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();
            taker.join(LOCKUP_DETECT_TIMEOUT);
            assertFalse(taker.isAlive());
        } catch (Exception e) {
            fail();
        }
    }
}
