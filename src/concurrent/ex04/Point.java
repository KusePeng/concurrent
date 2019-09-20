package concurrent.ex04;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock 乐观读升级为悲观读锁 练习
 */
public class Point {
    private int x, y;
    final StampedLock sl = new StampedLock();

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        long stamp = sl.writeLock();
        try {
            this.x = x;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    public void setY(int y) {
        long stamp = sl.writeLock();
        try {
            this.y = y;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    // 计算到原点的距离
    double distanceFromOrigin() {
        // 乐观读
        long stamp = sl.tryOptimisticRead();
        System.out.println(stamp);
        // 读入局部变量，
        // 读的过程数据可能被修改
        int curX = x, curY = y;
        // 判断执行读操作期间，
        // 是否存在写操作，如果存在，
        // 则 sl.validate 返回 false
        if (!sl.validate(stamp)) {
            // 升级为悲观读锁
            System.out.println("乐观读升级为悲观读锁");
            stamp = sl.readLock();
            try {
                curX = x;
                curY = y;
            } finally {
                // 释放悲观读锁
                sl.unlockRead(stamp);
            }
        }
        System.out.println(stamp);
        return Math.sqrt(curX * curX + curY * curY);
    }


    public static void main(String[] args) {
        Point point = new Point(3, 4);
        for(int i=0;i<10;i++){
            new Thread(()->{
                point.setX(10);
            }).start();
        }
        double v = point.distanceFromOrigin();
        System.out.println(v);

    }
}
