package com.zchun.chapter9;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chun.zhou
 * @date 2018/7/18 19:34
 */
public class GuiExecutor extends AbstractExecutorService {

    //Singleton 包含一个私有的构造函数和一个公共的工厂

    private static final GuiExecutor instance = new GuiExecutor();

    private GuiExecutor(){}

    public static GuiExecutor instance() {return instance;}

    @Override
    public void execute(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }

    // 添加其他有关生命周期方法的实现

    @Override
    public void shutdown() {

    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }



}
