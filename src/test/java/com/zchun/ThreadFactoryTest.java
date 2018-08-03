package com.zchun;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zchun.chapter12.TestingThreadFactory;

import junit.framework.TestCase;

/**
 * @author chun.zhou
 * @date 2018/7/28 16:18
 */
public class ThreadFactoryTest extends TestCase {

    TestingThreadFactory threadFactory = new TestingThreadFactory();

    public void testPoolExpansion() throws InterruptedException {
        final int MAX_SIZE = 10;
        ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE, threadFactory);

        for (int i = 0; i < 10 * MAX_SIZE; i++) {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        for (int i = 0; i < 20 && threadFactory.numCreated.get() < MAX_SIZE; i++) {
            Thread.sleep(100);
        }
        assertEquals(threadFactory.numCreated.get(), MAX_SIZE);
        exec.shutdownNow();
    }
}
