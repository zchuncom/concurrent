# 10.1 简单的锁顺序死锁（不要这样做）
> LeftRightDeadlock.java

![avatar](LeftRightDeadlock.png)

# 10.2 动态加锁顺序产生的死锁（不要这样做）
<pre>
<code>
//警告，易产生死锁
public void transferMoney(Account fromAccount, Account toAccount, DollarAmount amount) 
                         throws InsufficientFundsException {
    synchronized(fromAccount) {
        synchronized(toAccount) {
            
        }
    }
}
</code>
</pre>