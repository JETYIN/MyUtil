package utils.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/12/29.
 */

public class AtomicMethod {

    //提供原子性

    final static AtomicInteger integer = new AtomicInteger(1);

    /**
     * CAS原子性，硬件级别的原子性
     */
    public static int atomicMethod() {

        //创建原子性数据
        while (true) {

            int oldValue = integer.get();//获取创建AtomicInteger传入的值
            int newvalue = oldValue + 1;
            if (integer.compareAndSet(oldValue, newvalue)) {
                return newvalue;
            }

        }

    }

    /**
     * 同步实现原子性，所有需要访问该同步方法中的属性的线程都将被等待
     *
     * @return
     */
    public synchronized int synMethod() {

        int atomic = 0;
        return ++atomic;

    }

}
