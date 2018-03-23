package utils.synUtils;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2018/3/23.模拟启动程序时进行网络检查,可能有其他服务的检测，同样继承自BaseChecker
 */

public class NetWorkCheck extends BaseChecker {

    public NetWorkCheck(CountDownLatch countDownLatch) {
        super("network server", countDownLatch);
    }

    @Override
    public void checkServer() {//模拟runnable线程中启动检测服务

        System.out.println("Checking " + this.getServerName());
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getServerName() + " is UP");
    }
}
