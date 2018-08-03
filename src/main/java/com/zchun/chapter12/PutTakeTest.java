package com.zchun.chapter12;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

/**
 * @author chun.zhou
 * @date 2018/7/27 16:51
 */
public class PutTakeTest {
    private static final ExecutorService pool = Executors.newCachedThreadPool();
    private final AtomicInteger putSum = new AtomicInteger(0);
    private final AtomicInteger takeSum = new AtomicInteger(0);
    private final CyclicBarrier barrier;
    private final BoundedBuffer<Integer> bb;
    private final int nTrials, nPairs;

    private BarrierTimer timer = new BarrierTimer();

    public static void main(String[] args) {
        //sample paramters
        new PutTakeTest(10, 10, 100000).test();
        pool.shutdown();
    }

    PutTakeTest(int capacity, int npairs, int ntrials) {
        this.bb = new BoundedBuffer<>(capacity);
        this.nTrials = ntrials;
        this.nPairs = npairs;
        this.barrier = new CyclicBarrier(npairs * 2 + 1);
    }

    public void test2() {
        try {
            timer.clear();
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            barrier.await();
            barrier.await();
            long nsPerItem = timer.getTime() / (nPairs * (long) nTrials);
            System.out.println("Throughput:" + nsPerItem + " ns/item");
            TestCase.assertEquals(putSum.get(), takeSum.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void test() {

        try {
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            //等待所有线程做好准备
            barrier.await();
            //等待所有线程最终完成
            barrier.await();
            TestCase.assertEquals(putSum.get(), takeSum.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static int xorShift(int y) {
        y ^= (y << 6 );
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

    class Producer implements Runnable {
        @Override
        public void run() {
            try {
                int seed = (this.hashCode() ^ (int)System.nanoTime());
                int sum = 0;
                barrier.await();
                for (int i = 0; i < nTrials; i++) {
                    bb.put(seed);
                    sum += seed;
                    seed = xorShift(seed);
                }
                putSum.getAndAdd(sum);
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class Consumer implements Runnable {

        @Override
        public void run() {
            try {
                barrier.await();
                int sum = 0;
                for (int i = 0; i < nTrials; i++) {
                    sum += bb.take();
                }
                takeSum.getAndAdd(sum);
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

