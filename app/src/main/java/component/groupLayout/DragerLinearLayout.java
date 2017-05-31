package component.groupLayout;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/5/31.创建可拉拽的布局
 */

public class DragerLinearLayout extends LinearLayout {

    private ViewDragHelper dragHelper;

    public DragerLinearLayout(Context context) {
        super(context);
        dragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                /**设置为true打开可拖拽的view**/
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                /**返回移动边界信息**/
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {

                return top;
            }

            /**针对需要消费事件的组件-Button，textview设置了onclick后需要重写下列方法，否则拖动不会生效**/
            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }
}
