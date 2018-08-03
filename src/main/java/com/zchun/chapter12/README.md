# 12.1 利用Semaphore实现的有限缓存
> BoundedBuffer.java

# 12.2 BoundedBuffer的基本单元测试
> BoundedBufferTest.java

# 12.3 测试阻塞与响应中断
> BoundedBufferTest.java

# 12.4 适用于测试的中等品质的随机数生成器
<pre>
<code>
static int xorShift(int y) {
    y ^= (y << 6 );
    y ^= (y >>> 21);
    y ^= (y << 7);
    return y;
}
</code>
</pre>

# 12.5 BoundedBuffer的生产者-消费者测试程序
> PutTakeTest.java

# 12.6 PutTakeTest 中的生产者和消费者
> PutTakeTest.java

# 12.7 测试资源泄露
<pre>
<code>
class Big {double[] data = new double[100000];}
void testLeak() throws InterruptedException {
    BoundedBuffer&lt;Big&gt; bb = new BoundedBuffer&lt;Big&gt;(CAPACITY);
    int heapSize 1 = /*heap的快照*/;
    for (int i=0; i < CAPACITY; i++ ) 
        bb.put(new Big());
    for (int i=0; i < CAPACITY; i++ ) 
        bb.take();
    int heapSize2 = /*heap的快照*/;
    assertTrue(Math.abs(heapSize1-heapSize2) < THRESHOLD);        
}
</code>
</pre>

# 12.8 用于测试ThreadPoolExecutor的线程工厂
> TestingThreadFactory.java

# 12.9 验证线程池扩展的测试方法
> ThreadFactoryTest

# 12.10 使用Thread.yield产生更多的交替操作
<pre>
<code>
public synchronized void transferCredits(Account from, 
                                         Account to,  int amount) {
    from.setBalance(from.getBalance() - amount);
    if (random.nextInt(1000) > THRESHOLD) {
        Thread.yield();
    }
    to.setBalance(to.getBalance() + amount);    
}
</code>
</pre>

# 12.11 基于关卡的计时器
> BarrierTimer.java

# 12.12 使用基于关卡的计时器进行测试
> PutTakeTest.java#test2

