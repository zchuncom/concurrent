package com.zchun.chapter9;

import java.util.concurrent.*;

/**
 * @author chun.zhou
 * @date 2018/7/18 19:23
 */
public abstract class BackgroundTask<V> implements Runnable, Future<V> {

    private final FutureTask<V> computation = new Computation();

    private class Computation extends FutureTask<V> {

        public Computation() {
            super(new Callable<V>() {
                @Override
                public V call() throws Exception {
                    //return BackgroundTask.this.computation;
                    return null;
                }
            });
        }

        @Override
        protected final void done() {
            GuiExecutor.instance().execute(new Runnable() {
                @Override
                public void run() {
                    V value = null;
                    Throwable thrown = null;
                    boolean cancelled = false;
                    try {
                        value = get();
                    } catch (ExecutionException e) {
                        thrown = e;
                    } catch (CancellationException e) {
                        cancelled = true;
                    } catch (InterruptedException e) {
                    } finally {
                        onCompletion(value, thrown, cancelled);
                    }
                }
            });
        }
    }
    protected  void setProgress(final int current, final int max) {
        GuiExecutor.instance().execute(new Runnable() {
            @Override
            public void run() {
                onProgress(current, max);
            }
        });
    }
    /**
     * 在后台线程中调用
     * @return
     * @throws Exception
     */
    protected abstract V compute() throws Exception;

    /**
     * 在事件线程中调用
     * @param result
     * @param exception
     * @param cancelled
     */
    protected void onCompletion(V result, Throwable exception, boolean cancelled) {}

    protected void onProgress(int current, int max) {}

    //其他用于完成计算的方法
}
