package concurrent.ex01;

import java.util.concurrent.CountDownLatch;

/**
 * @author xp
 * @Describe
 * @create 2019-09-16
 */
public class MyLock {
    public static void main(String[] args) throws InterruptedException {
        Account src = new Account(10000);
        Account target = new Account(10000);
        CountDownLatch countDownLatch = new CountDownLatch(9999);
        for (int i = 0; i < 9999; i++) {
            new Thread(()->{
                src.transactionToTarget(1,target);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println("src="+src.getBalance() );
        System.out.println("target="+target.getBalance() );
    }

}