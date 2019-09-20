package concurrent.ex03;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author xp
 * @Describe 读写锁练习
 * @create 2019-09-20
 */
public class Cache<K, V> {

    final Map<K, V> m = new HashMap<>();
    final ReadWriteLock rwl = new ReentrantReadWriteLock();
    // 读锁
    final Lock r = rwl.readLock();
    // 写锁
    final Lock w = rwl.writeLock();

    // 读缓存
    V get(K key) {
        r.lock();
        try {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return m.get(key);
        } finally {
            r.unlock();
        }
    }

    // 写缓存
    V put(K key, V value) {
        w.lock();
        try {
            return m.put(key, value);
        } finally {
            w.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Cache<String, String> cache = new Cache<>();
        Set<String> set = new CopyOnWriteArraySet<>();
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                set.add("key_" + Thread.currentThread().getId());
                cache.put("key_" + Thread.currentThread().getId(), "value_" + Thread.currentThread().getName());
                latch.countDown();
            }).start();
        }

        latch.await();

        for (String key : set) {
            for (int i = 0; i < 3; i++) {
                new Thread(() -> {
                    String value = cache.get(key);
                    System.out.println(value);
                }).start();
            }

        }
    }
}
