package component.groupLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.work.service.permisson.R;

/**
 * Created by Administrator on 2017/5/31.
 */

public class VolCircle extends View {

    private int mFirstColor;//第一圈颜色
    private int mSencendColor;//第二圈颜色
    private int mCircleSize;//圆形大小
    private int dotCount;//圆环分段大小
    private int interval;//圆环间隙

    private Bitmap mBitmap;//背景图片

    //设置画笔
    private Paint mPaint;
    private Rect mRect;


    //
    private int mCurrentPosition = 3;//当前进度

    //touch事件的按下，松开位置记录
    private int yDown, yUp;

    public VolCircle(Context context) {
        this(context, null);
    }

    public VolCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolCircle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VolCircle, defStyle, 0);
        initTypes(ta);//获取资源文件设置数据

        mPaint = new Paint();
        mRect = new Rect();
        ta.recycle();
    }


    private void initTypes(TypedArray typedArray) {

        mFirstColor = typedArray.getColor(R.styleable.VolCircle_firstColor, Color.BLACK);
        mSencendColor = typedArray.getColor(R.styleable.VolCircle_secendColor, Color.CYAN);

        mCircleSize = typedArray.getDimensionPixelSize(R.styleable.VolCircle_circleSize, 0);

        dotCount = typedArray.getInt(R.styleable.VolCircle_dotCount, 10);
        interval = typedArray.getInt(R.styleable.VolCircle_interval, 5);

        /**此处不设置drawable默认值为绝对路径会报null**/
        mBitmap = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(R.styleable.VolCircle_backGroud, 0));

    }

    @Override
    protected void onDraw(Canvas canvas) {


        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStrokeWidth(mCircleSize); // 设置圆环的宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 定义线段断电形状为圆头
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
        int center = getWidth() / 2; // 获取圆心的x坐标
        int radius = center - mCircleSize / 2;// 半径

        //调用方法
        drawOval(canvas, center, radius);

        /**
         * 计算内切正方形的位置
         */
        int relRadius = radius - mCircleSize / 2;// 获得内圆的半径
        /**
         * 内切正方形的距离顶部 = mCircleWidth + relRadius - √2 / 2
         */
        mRect.left = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleSize;
        /**
         * 内切正方形的距离左边 = mCircleWidth + relRadius - √2 / 2
         */
        mRect.top = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleSize;
        mRect.bottom = (int) (mRect.left + Math.sqrt(2) * relRadius);
        mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius);

        /**
         * 如果图片比较小，那么根据图片的尺寸放置到正中心
         */

        if (mBitmap.getWidth() < Math.sqrt(2) * relRadius) {
            mRect.left = (int) (mRect.left + Math.sqrt(2) * relRadius * 1.0f / 2 - mBitmap.getWidth() * 1.0f / 2);
            mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - mBitmap.getHeight() * 1.0f / 2);
            mRect.right = mRect.left + mBitmap.getWidth();
            mRect.bottom = mRect.top + mBitmap.getHeight();
        }

        // 绘图
        canvas.drawBitmap(mBitmap, null, mRect, mPaint);
    }


    private void drawOval(Canvas canvas, int centre, int radius) {
        /**
         * 根据需要画的个数以及间隙计算每个块块所占的比例*360
         */
        float itemSize = (360 * 1.0f - dotCount * interval) / dotCount;
        /**drawArc专用于绘制圆弧**/
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限

        mPaint.setColor(mFirstColor); // 设置圆环的颜色
        for (int i = 0; i < dotCount; i++) {
            canvas.drawArc(oval, i * (itemSize + interval), itemSize, false, mPaint); // 根据进度画圆弧
        }

        mPaint.setColor(mSencendColor); // 设置圆环的颜色
        for (int i = 0; i < mCurrentPosition; i++) {
            canvas.drawArc(oval, i * (itemSize + interval), itemSize, false, mPaint); // 根据进度画圆弧
        }
    }

    //手指按下-音量指数加1.界面更新
    private void fingerDown() {
        if (mCurrentPosition == dotCount) {
            return;
        }
        mCurrentPosition++;
        postInvalidate();

    }

    //手指松开
    private void fingerUp() {

        /**当减为0时不再减了--不设置即会一直递减**/
        if (mCurrentPosition == 0) {
            return;
        }
        mCurrentPosition--;
        postInvalidate();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                yDown = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                yUp = (int) event.getY();
                //将在y轴上的distance进行判断
                if (yUp > yDown) {
                    //这是一个下滑操作
                    fingerUp();
                } else {
                    fingerDown();
                }

                break;
        }
        return true;
    }

    /**在使用时，onMeasure方法，在xml中设置确定的值以及matchParent时没有问题，
     * 但是将其设置为wrap_content时必须要重写onMeasure方法，不然整个布局将会按照match_Parent进行**/
}
