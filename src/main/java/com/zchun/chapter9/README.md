9.1 使用Executor实现的SwingUtilities
SwingUtilities.java

9.2 构建于SwingUtilities之上的Executor 
GuiExecutor.java

9.3 简单的事件监听器
final Random random = new Random();
final JButton button = new JButton("Change Color");
...
button.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        button.setBackground(new Color(random.nextInt()));
    }
});

9.4 将耗时任务绑定到可视化组件
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

9.5 提供用户反馈的耗时任务
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

9.6 取消耗时任务
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

9.7 支持取消、完成和进度通知的后台任务类
BackgroundTask.java