package utils.synUtils;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2018/3/23.创建解决同步检查资源是否准备好，未准备好不启动程序
 */

public abstract class BaseChecker implements Runnable {

    protected String serverName;
    protected CountDownLatch countDownLatch;//闭锁的线程数量集合，加入其中的线程执行结束后才会打开锁
    protected boolean isServerUp;

    public BaseChecker(String serverName, CountDownLatch countDownLatch) {
        this.serverName = serverName;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {

        try {
            checkServer();
            isServerUp = true;
        } catch (Exception e) {
            isServerUp = false;
        } finally {
            if (null != countDownLatch) {
                countDownLatch.countDown();//最后停止计数
            }
        }
    }

    public String getServerName() {
        return serverName;
    }

    public boolean getServerUp() {
        return isServerUp;
    }

    public abstract void checkServer();
}
