package com.example.appdemo;

import java.util.ArrayList;
import java.util.Random;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class RaderView extends FrameLayout {

	private Context mContext;
	private int viewSize = 800;
	private Paint mPaintLine;
	private Paint mPaintSector;
	public boolean isstart = false;
	private ScanThread mThread;
	private Paint mPaintPoint;
	private RectF mRectF;
	private int start = -90;
	private int end = 90;
	
	private boolean isTimeNow;

	public RaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		initPaint();
		mThread = new ScanThread(this);
		setBackgroundColor(Color.TRANSPARENT);
	}

	public RaderView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		initPaint();
		mThread = new ScanThread(this);
		setBackgroundColor(Color.TRANSPARENT);
	}

	private void initPaint() {
		// TODO Auto-generated method stub
		mPaintLine = new Paint();
		mPaintLine.setStrokeWidth(10);
		mPaintLine.setAntiAlias(true);
		mPaintLine.setStyle(Style.STROKE);
		mPaintLine.setColor(0xff000000);

		mPaintSector = new Paint();
		mPaintSector.setColor(0x9D00ff00);
		mPaintSector.setAntiAlias(true);
	}

	/** 设置绘制尺寸 **/
	public void setViewSize(int size) {
		this.viewSize = size;
		setMeasuredDimension(viewSize, viewSize);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(viewSize, viewSize);
	}

	public void start() {
		mThread.start();
		isstart = true;
	}

	public void stop() {
		if (isstart) {
			Thread.interrupted();
			isstart = false;
		}
	}

	@SuppressLint("NewApi")
	public void setList(ArrayList<View> list) {
		for (int i = 0; i < list.size(); i++) {
			int xy[] = getRamdomXY();
			list.get(i).setX(xy[0]);
			list.get(i).setY(xy[1]);
			addView(list.get(i));
		}
	}

	private int[] getRamdomXY() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int x = rand.nextInt(900);
		int y = rand.nextInt(900);
		int r = (int) ((float) viewSize / 2);
		if ((x >= r - 350 || x <= r + 350) && (y >= r - 350 || y <= r + 350)) {
			int xy[] = new int[2];
			xy[0] = x;
			xy[1] = y;
			return xy;
		} else {
			return getRamdomXY();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawCircle(viewSize / 2, viewSize / 2, 175, mPaintLine);
		canvas.drawCircle(viewSize / 2, viewSize / 2, 350, mPaintLine);
		canvas.drawLine(viewSize / 2, 0, viewSize / 2, viewSize, mPaintLine);
		canvas.drawLine(0, viewSize / 2, viewSize, viewSize / 2, mPaintLine);
		/** 实例化扫描，梯度渲染 **/
		Shader mShader = new SweepGradient(viewSize / 2, viewSize / 2,
				Color.TRANSPARENT, Color.GREEN);
		mPaintSector.setShader(mShader);
		canvas.concat(matrix);
		canvas.drawCircle(viewSize / 2, viewSize / 2, 350, mPaintSector);
		super.onDraw(canvas);
	}

	private Matrix matrix;

	protected class ScanThread extends Thread {

		private RaderView view;

		public ScanThread(RaderView view) {
			// TODO Auto-generated constructor stub
			this.view = view;
		}

		
		/**判断是否到了冷却时间**/
		
		private void isColdTime(){
			
			
			
		}
		/**
		 * Matrix 
		 * 1、Translate————平移变换
		 * 
		 * 2、Scale————缩放变换
		 * 
		 * 3、Rotate————旋转变换
		 * 
		 * 4、Skew————错切变换
		 **/
		@Override
		public void run() {
			// TODO Auto-generated method stub
			/** 默认设置一直执行,可以手动设置时间计时 **/
			while (true) {
				if (isstart) {
					view.post(new Runnable() {
						public void run() {
							/** start的初始值为0，开始启动线程时从0开始一直执行到转完一个圆形圈 **/
							start = start + 1;
							matrix = new Matrix();
							/** 设置旋转变化 ,**/
							matrix.postRotate(start, viewSize / 2, viewSize / 2);
							/** 线程每次start值得变化引起的matrix变化，调用view的进行绘制 **/
							view.invalidate();
						}
					});
					try {
						/**设置没转过一个角度20ms休眠**/
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

}
