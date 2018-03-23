package utils.synUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/3/23.作为封装好的接口类，所有检测服务将从此处返回是否可用
 */

public class AppUtils {

    private List<BaseChecker> list;
    private CountDownLatch countDownLatch;
    private static AppUtils instance;

    public static AppUtils getInstance() {
        if (null == instance) {
            synchronized (AppUtils.class) {
                if (null == instance) {
                    instance = new AppUtils();
                }
            }
        }
        return instance;
    }

    public boolean checkSystemResult() {
        list = new ArrayList<>();
        countDownLatch = new CountDownLatch(1);//可维持一个同步池
        //多个check加入集合进行检查，等所有检查结束返回true
        list.add(new NetWorkCheck(countDownLatch));
//------
        Executor executor = Executors.newFixedThreadPool(list.size());//创建线程池
        for (BaseChecker checker : list) {

            executor.execute(checker);
        }

        for(BaseChecker checker : list){//所有子系统检查完毕即可返回true，释放锁
            if(!checker.getServerUp()){
                return false;
            }
        }

            return true;
    }
}
