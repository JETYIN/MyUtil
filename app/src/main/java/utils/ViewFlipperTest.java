package utils;

/**
 * Created by Administrator on 2018/1/30.
 */

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前类用作于实现--网络商城的广告滚动(垂直方向)，textview的跑马灯特效，类似于新闻滚动播报
 **/
public class ViewFlipperTest {

    private ViewFlipper viewFlipper;
    private int count;

    /**
     * viewFlipper的说明，主要是在xml中设置好对应节点的自动开始滚动、每次滚动的时间间隔、item滚入动画、item滚出动画
     */
    private void init() {

        List<String> testList = new ArrayList<>();
        testList.add("爸妈爱的“白”娃娃，真是孕期吃出来的吗？");
        testList.add("如果徒步真的需要理由，十四个够不够？");
        testList.add("享受清爽啤酒的同时，这些常识你真的了解吗？");
        testList.add("三星Galaxy S8定型图无悬念");
        testList.add("家猫为何如此高冷？");
        count = testList.size();
        for (int i = 0; i < count; i++) {

            final View ll_content = View.inflate(FlipActivity.this, R.layout.item_fliper, null);
            TextView tv_content = (TextView) ll_content.findViewById(R.id.tv_content);
            ImageView iv_closebreak = (ImageView) ll_content.findViewById(R.id.iv_closebreak);
            tv_content.setText(testList.get(i).toString());
            // 以上设置滚动的item以及对应的文字内容
            viewFlipper.addView(ll_content);// xml节点设置了自动开始，故而java代码中只需将view加入viewFlipper即可
        }
    }


    /**
     * 针对跑马灯的textview，只需要在textview中的xml设置对应节点的以及节点属性，即可实现文字的跑马灯，但是该textview的父布局需要为LineaerLayout,RelativeLayout也可以进行跑马灯，但是运行时显示异常
     */
    private void initTextView() {
    }

}


}
