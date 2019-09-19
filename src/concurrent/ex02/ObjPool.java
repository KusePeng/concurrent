package concurrent.ex02;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 信号量实现限流器,每次能够获得信号量的线程才能执行
 * @param <T>
 * @param <R>
 */
public class ObjPool<T, R> {
    final List<T> pool;
    // 用信号量实现限流器
    final Semaphore sem;
    // 构造函数
    public ObjPool(int size, T t) {
        pool = new Vector<T>();
        for (int i = 0; i < size; i++) {
            pool.add(t);
        }
        sem = new Semaphore(size);
    }

    // 利用对象池的对象，调用 func
    R exec(Function<T, R> func) throws InterruptedException {
        T t = null;
        sem.acquire();
        try {
            t = pool.remove(0);
            TimeUnit.SECONDS.sleep(2);
            return func.apply(t);
        } finally {
            pool.add(t);
            sem.release();
        }
    }

    public static void main(String[] args) {
        // 创建对象池
        ObjPool<Long, String> pool = new ObjPool<>(2, 2L);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 通过对象池获取 t，之后执行
        for(int i=0;i<10;i++){
            executorService.submit(()->{
                try {
                    pool.exec(t->{
                        System.out.println(t);
                        return t.toString();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
    }
}
