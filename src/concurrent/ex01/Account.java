package concurrent.ex01;

/**
 * @author xp
 * @Describe 账户信息
 * @create 2019-09-16
 */
public class Account {
    public Account(Integer balance) {
        this.balance = balance;
    }

    private Integer balance;
    //转账方法
    public void transactionToTarget(Integer money, Account target) {
        Allocator.getInstance().apply(this, target);
        this.balance -= money;
        target.setBalance(target.getBalance() + money);
        Allocator.getInstance().release(this, target);
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
