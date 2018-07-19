# 9.1 使用Executor实现的SwingUtilities
> SwingUtilities.java

# 9.2 构建于SwingUtilities之上的Executor 
> GuiExecutor.java

# 9.3 简单的事件监听器
<pre>
<code>
final Random random = new Random();
final JButton button = new JButton("Change Color");
...
button.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        button.setBackground(new Color(random.nextInt()));
    }
});
</code>
</pre>

# 9.4 将耗时任务绑定到可视化组件
<pre>
<code>
ExecutorService backgroundExec = Executors.newCachedThreadPool();
...
button.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        backgroundExec.execute(new Runnable() {
            public void run() {
                doBigComputation();
            };
        });
    }
});
</code>
</pre>

# 9.5 提供用户反馈的耗时任务
<pre>
<code>
button.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        button.setEnabled(false);
        label.setText("busy");
        backgroundExec.execute(new Runnable() {
            public void run() {
                try {
                    doBigComputation();
                } finally {
                    GuiExecutor.instance().execute(new Runnable() {
                        public void run() {
                            button.setEnabled(true);
                            label.setText("idle");
                        }
                    })
                }
            }
        });
    }
});
</code>
</pre>

# 9.6 取消耗时任务
<pre>
<code>
Future<?> runningTask = null; //线程限制的
...
startButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        if ( runningTask == null ) {
            runningTask = backgroundExec.submit(new Runnable() {
                public void run() {
                    while(moreWork()) {
                        if (Thread.interrupted()) {
                            cleanUpPartialWork();
                            break;
                        }
                        doSomeWork();
                    }
                }
            })
        }
    }
});

cancelButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        if (runningTask != null)
            runningTask.cancel(true);
    }
});
</code>
</pre>

# 9.7 支持取消、完成和进度通知的后台任务类
> BackgroundTask.java

# 9.8 在BackgroundTask中启动一个耗时的、可取消的任务
<pre>
<code>
startButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        class CancelListener implements ActionListener {
            BackgroundTask<?> task;
            public void actionPerformed(ActionEvent event) {
                if (task != null) {
                    task.cancel(true);
                }
            }
        }
        final CancelListener listener = new CancelListener();
        listener.task = new BackgroundTask<Void>() {
            public void compute() {
                while(moreWork() && !isCancelled()) {
                    doSmoeWork();
                }
                return null;
            }
            public void onCompletion(boolean cancelled, String s, Throwable exception) {
                cancelButton.removeActionListener(listener);
                label.setText("done");
            }
        }
        cancelButton.addActionListener(listener);
        backgroundExec.execute(listener.task);
    }
});
</code>
</pre>