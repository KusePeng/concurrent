package concurrent.ex01;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xp
 * @Describe 账户管理器
 * @create 2019-09-16
 */
public class Allocator {
    private Allocator() {
    }

    private List<Account> locks = new ArrayList<>();

    public synchronized void apply(Account src, Account tag) {
        while (locks.contains(src) || locks.contains(tag)) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
        }
        locks.add(src);
        locks.add(tag);
    }

    public synchronized void release(Account src, Account tag) {
        locks.remove(src);
        locks.remove(tag);
        this.notifyAll();
    }

    public static Allocator getInstance() {
        return AllocatorSingle.install;
    }

    static class AllocatorSingle {
        public static Allocator install = new Allocator();
    }
}
