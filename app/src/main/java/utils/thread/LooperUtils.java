package utils.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by Administrator on 2017/8/8.
 */

public class LooperUtils {
    /**
     * 让普通线程变为维系消息队列的循环线程
     **/
    private class WorkThread extends Thread {

        private Handler mHandler;

        private Looper mLooper;

        public WorkThread() {
            start();
        }

        @Override
        public void run() {
            Looper.prepare();
            mLooper = Looper.myLooper();
            mHandler = new Handler(mLooper) {
                @Override
                public void handleMessage(Message msg) {

                }
            };
            Looper.loop();
        }

        //线程循环结束时
        public void exit() {
            if (mLooper != null) {
                mLooper.quit();
                mLooper = null;
            }
        }
    }

}
