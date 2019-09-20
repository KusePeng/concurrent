package concurrent.ex04;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.StampedLock;

/**
 * @author xp
 * @Describe StampedLock 中使用中断导致CPU使用率飙升测试
 * @create 2019-09-20
 */
public class StampedLockInterrup {
    static final StampedLock lock = new StampedLock();


    public static void main(String[] args) throws InterruptedException {

        Thread T1 = new Thread(() -> {
            // 获取写锁
            lock.writeLock();
            // 永远阻塞在此处，不释放写锁
            LockSupport.park();
        });
        T1.start();
        // 保证 T1 获取写锁
        Thread.sleep(100);
        Thread T2 = new Thread(() ->
            // 阻塞在悲观读锁
            lock.readLock()
        );
        T2.start();
        // 保证 T2 阻塞在读锁
        Thread.sleep(100);
        // 中断线程 T2
        // 会导致线程 T2 所在 CPU 飙升
        T2.interrupt();
        T2.join();
    }

}
