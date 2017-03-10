package utils;

import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/3/10.
 */

public class MyLayoutParams {

    public static MyLayoutParams instance;

    private MyLayoutParams() {
    }

    public static MyLayoutParams getInstance() {
        if (instance == null) {
            synchronized (MyLayoutParams.class) {
                if (instance == null) {
                    instance = new MyLayoutParams();
                }
            }
        }
        return instance;
    }


    /**
     * LayoutPatams使用方法，所有布局的Layoutparams均是继承自viewgroup的内部类LayoutParams
     **/
    public void init() {
        //对布局的宽高进行设置
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //可以对其进行参数设置，最后添加运用---addview(textview,layoutparams);

    }
}
