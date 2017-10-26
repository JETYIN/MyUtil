package utils.autoLogin;

import java.util.Timer;
import java.util.TimerTask;

import com.haowan123.funcell.sdk.ui.FunSdkUiActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

public class FloatView extends FrameLayout implements View.OnTouchListener {

	private String TAG = "FloatView";
	private final int HANDLER_TYPE_HIDE_LOGO = 100;
	private final int HANDLER_TYPE_CANCEL_ANIM = 101;
	private WindowManager.LayoutParams mWmParams;
	private WindowManager mWindowManager;
	private Context mContext;
	private ImageView mIvFloatLogo;
	private LinearLayout mLlFloatMenu;
	private ImageButton btnAccount;
	private ImageButton btnHide;
	private ImageView mIvFloatLoader;

	private FrameLayout mFlFloatLogo;
	private boolean mIsRight;
	private boolean mCanHide;
	private float mTouchStartX;
	private float mTouchStartY;
	private int mScreenWidth;
	private int mScreenHeight;
	private boolean mDraging;
	private boolean mShowLoader = true;
	private Timer mTimer;
	private TimerTask mTimerTask;
	final Handler mTimerHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == HANDLER_TYPE_HIDE_LOGO) {
				if (mCanHide) {
					mCanHide = false;
					if (mIsRight) {
						mIvFloatLogo.setImageResource(RUtils.drawable(mContext, "fun_right_hide"));
						/** notice右边隐藏时的图片资源 **/
					} else {
						mIvFloatLogo.setImageResource(RUtils.drawable(mContext, "fun_left_hide"));
						/** notice左边隐藏时的图片资源 **/
					}
					mWmParams.alpha = 0.5F;
					mWindowManager.updateViewLayout(FloatView.this, mWmParams);
					refreshFloatMenu(mIsRight);
					mLlFloatMenu.setVisibility(View.GONE);
				}
			} else if (msg.what == HANDLER_TYPE_CANCEL_ANIM) {
				mIvFloatLoader.clearAnimation();
				mIvFloatLoader.setVisibility(View.GONE);
				mShowLoader = false;
			}
			super.handleMessage(msg);
		}
	};

	public FloatView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mContext = context;

		mWindowManager = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE));

		DisplayMetrics dm = new DisplayMetrics();

		mWindowManager.getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		mWmParams = new WindowManager.LayoutParams();
		// 设置window type
		mWmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		// mWmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
		// } else {
		// mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		// }
		// 设置图片格式，效果为背景透明
		mWmParams.format = PixelFormat.RGBA_8888;

		// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
		mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		// 调整悬浮窗显示的停靠位置为左侧置�?
		mWmParams.gravity = Gravity.LEFT | Gravity.TOP;

		mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();

		// 以屏幕左上角为原点，设置x、y初始值，相对于gravity
		mWmParams.x = 0;
		mWmParams.y = (mScreenHeight / 2);
		// 设置悬浮窗口长宽数据
		mWmParams.width = LayoutParams.WRAP_CONTENT;
		mWmParams.height = LayoutParams.WRAP_CONTENT;
		addView(createView(mContext));
		mWindowManager.addView(this, mWmParams);

		mTimer = new Timer();
		hide();
	}

	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		DisplayMetrics dm = new DisplayMetrics();

		mWindowManager.getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		int oldX = mWmParams.x;
		int oldY = mWmParams.y;
		switch (newConfig.orientation) {
		case Configuration.ORIENTATION_LANDSCAPE:
			if (mIsRight) {
				mWmParams.x = mScreenWidth;
				mWmParams.y = oldY;
			} else {
				mWmParams.x = oldX;
				mWmParams.y = oldY;
			}
			break;
		case Configuration.ORIENTATION_PORTRAIT:
			if (mIsRight) {
				mWmParams.x = mScreenWidth;
				mWmParams.y = oldY;
			} else {
				mWmParams.x = oldX;
				mWmParams.y = oldY;
			}
			break;
		}
		mWindowManager.updateViewLayout(this, mWmParams);
	}

	private View createView(final Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);

		/** notice悬浮窗主页面 **/
		View rootFloatView = inflater.inflate(RUtils.layout(context, "raink_widget_float_view"), null);
		// framLayout
		mFlFloatLogo = ((FrameLayout) rootFloatView.findViewById(RUtils.id(context, "raink_float_view")));
		// mian image
		mIvFloatLogo = ((ImageView) rootFloatView.findViewById(RUtils.id(context, "raink_float_view_icon_imageView")));

		mIvFloatLoader = ((ImageView) rootFloatView.findViewById(RUtils.id(context, "raink_float_view_icon_notify")));

		/** notice弹出界面lin **/
		mLlFloatMenu = ((LinearLayout) rootFloatView.findViewById(RUtils.id(context, "ll_menu")));

		/** notice用户 **/
		btnAccount = (ImageButton) (rootFloatView.findViewById(RUtils.id(context, "buttonAccount")));

		btnAccount.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				openUserCenter(context);
				mLlFloatMenu.setVisibility(View.GONE);
			}
		});

		/** notice隐藏悬浮窗 **/

		btnHide = (ImageButton) (rootFloatView.findViewById(RUtils.id(context, "buttonHide")));
		// 隐藏悬浮窗
		btnHide.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				showHideComfirmDl(context);
				mLlFloatMenu.setVisibility(View.GONE);
			}
		});

		rootFloatView.setOnTouchListener(this);
		rootFloatView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!mDraging) {
					if (mLlFloatMenu.getVisibility() == View.VISIBLE) {
						mLlFloatMenu.setVisibility(View.GONE);
					} else {
						mLlFloatMenu.setVisibility(View.VISIBLE);
					}
				}
			}
		});
		rootFloatView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

		return rootFloatView;
	}

	private void showHideComfirmDl(final Context context) {

		((Activity) context).runOnUiThread(new Runnable() {

			public void run() {
				FunCustomDialog.Builder builder = new FunCustomDialog.Builder(context);
				builder.setMessage(
						context.getResources().getString(RUtils.string(context, "fun_error_page_dialog_msg_title")));
				builder.setTitle(context.getResources().getString(RUtils.string(context, "fun_error_page_head_title")));
				builder.setPositiveButton(
						context.getResources()
								.getString(RUtils.string(context, "fun_error_page_dialog_btn_sure_title")),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								hide();
							}
						});

				builder.setNegativeButton(
						context.getResources()
								.getString(RUtils.string(context, "fun_error_page_dialog_btn_cancel_title")),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();

							}
						});

				builder.create().show();

			}
		});
	}

	// 打开用户中心
	private void openUserCenter(Context context) {
		Intent intent = new Intent(context, FunSdkUiActivity.class);
		intent.putExtra("fun_action", "usercenter");
		context.startActivity(intent);
	}

	public boolean onTouch(View v, MotionEvent event) {
		removeTimerTask();

		int x = (int) event.getRawX();
		int y = (int) event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mTouchStartX = event.getX();
			mTouchStartY = event.getY();
			mIvFloatLogo.setImageResource(RUtils.drawable(mContext, "fun_default_icon"));

			mWmParams.alpha = 1.0F;
			mWindowManager.updateViewLayout(this, mWmParams);
			mDraging = false;
			break;
		case MotionEvent.ACTION_MOVE:
			float mMoveStartX = event.getX();
			float mMoveStartY = event.getY();
			if ((Math.abs(mTouchStartX - mMoveStartX) > 3.0F) && (Math.abs(mTouchStartY - mMoveStartY) > 3.0F)) {
				mDraging = true;

				mWmParams.x = ((int) (x - mTouchStartX));
				mWmParams.y = ((int) (y - mTouchStartY));
				mWindowManager.updateViewLayout(this, mWmParams);
				mLlFloatMenu.setVisibility(View.GONE);
				return false;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (mWmParams.x >= mScreenWidth / 2) {
				mWmParams.x = mScreenWidth;
				mIsRight = true;
			} else if (mWmParams.x < mScreenWidth / 2) {
				mIsRight = false;
				mWmParams.x = 0;
			}
			mIvFloatLogo.setImageResource(RUtils.drawable(mContext, "fun_default_icon"));

			refreshFloatMenu(mIsRight);
			timerForHide();
			mWindowManager.updateViewLayout(this, mWmParams);

			mTouchStartX = (mTouchStartY = 0.0F);
		}
		return false;
	}

	private void removeTimerTask() {
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}

	private void removeFloatView() {
		try {
			mWindowManager.removeView(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void hide() {
		setVisibility(View.GONE);
		Message message = mTimerHandler.obtainMessage();
		message.what = HANDLER_TYPE_HIDE_LOGO;
		mTimerHandler.sendMessage(message);
		removeTimerTask();
	}

	public void show() {
		if (getVisibility() != View.VISIBLE) {
			setVisibility(View.VISIBLE);
			if (mShowLoader) {
				mIvFloatLogo.setImageResource(RUtils.drawable(mContext, "fun_default_icon"));

				mWmParams.alpha = 1.0F;
				mWindowManager.updateViewLayout(this, mWmParams);

				timerForHide();

				mShowLoader = false;

				Animation rotaAnimation = AnimationUtils.loadAnimation(mContext,
						RUtils.anim(mContext, "raink_loading_anim"));

				rotaAnimation.setInterpolator(new LinearInterpolator());
				mIvFloatLoader.startAnimation(rotaAnimation);

				// 不重复执行，延迟2000毫秒后执行task
				mTimer.schedule(new TimerTask() {
					public void run() {
						mTimerHandler.sendEmptyMessage(HANDLER_TYPE_CANCEL_ANIM);
					}
				}, 2000L);
			}
		}
	}

	private void refreshFloatMenu(boolean right) {
		if (right) {
			LayoutParams paramsFloatImage = (LayoutParams) mIvFloatLogo.getLayoutParams();
			paramsFloatImage.gravity = 5;
			mIvFloatLogo.setLayoutParams(paramsFloatImage);
			LayoutParams paramsFlFloat = (LayoutParams) mFlFloatLogo.getLayoutParams();
			paramsFlFloat.gravity = 5;
			mFlFloatLogo.setLayoutParams(paramsFlFloat);

			int padding = (int) TypedValue.applyDimension(1, 4.0F, mContext.getResources().getDisplayMetrics());
			int padding52 = (int) TypedValue.applyDimension(1, 52.0F, mContext.getResources().getDisplayMetrics());
			LinearLayout.LayoutParams paramsMenuAccount = (LinearLayout.LayoutParams) btnAccount.getLayoutParams();
			paramsMenuAccount.rightMargin = padding;
			paramsMenuAccount.leftMargin = padding;
			btnAccount.setLayoutParams(paramsMenuAccount);

			LinearLayout.LayoutParams paramsMenuFb = (LinearLayout.LayoutParams) btnHide.getLayoutParams();
			paramsMenuFb.rightMargin = padding52;
			paramsMenuFb.leftMargin = padding;
			btnHide.setLayoutParams(paramsMenuFb);
		} else {
			LayoutParams params = (LayoutParams) mIvFloatLogo.getLayoutParams();
			params.setMargins(0, 0, 0, 0);
			params.gravity = 3;
			mIvFloatLogo.setLayoutParams(params);
			LayoutParams paramsFlFloat = (LayoutParams) mFlFloatLogo.getLayoutParams();
			paramsFlFloat.gravity = 3;
			mFlFloatLogo.setLayoutParams(paramsFlFloat);

			int padding = (int) TypedValue.applyDimension(1, 4.0F, mContext.getResources().getDisplayMetrics());
			int padding52 = (int) TypedValue.applyDimension(1, 52.0F, mContext.getResources().getDisplayMetrics());

			LinearLayout.LayoutParams paramsMenuAccount = (LinearLayout.LayoutParams) btnAccount.getLayoutParams();
			paramsMenuAccount.rightMargin = padding;
			paramsMenuAccount.leftMargin = padding52;
			btnAccount.setLayoutParams(paramsMenuAccount);

			LinearLayout.LayoutParams paramsMenuFb = (LinearLayout.LayoutParams) btnHide.getLayoutParams();
			paramsMenuFb.rightMargin = padding;
			paramsMenuFb.leftMargin = padding;
			btnHide.setLayoutParams(paramsMenuFb);
		}
	}

	// 图标显示后多久消失
	private void timerForHide() {
		mCanHide = true;
		if (mTimerTask != null) {
			try {
				mTimerTask.cancel();
				mTimerTask = null;
			} catch (Exception localException) {
			}
		}
		mTimerTask = new TimerTask() {
			public void run() {
				Message message = mTimerHandler.obtainMessage();
				message.what = 100;
				mTimerHandler.sendMessage(message);
			}
		};
		if (mCanHide) {
			mTimer.schedule(mTimerTask, 3500L, 2000L);
		}
	}

	public void destroy() {
		hide();
		removeFloatView();
		removeTimerTask();
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		try {
			mTimerHandler.removeMessages(1);
		} catch (Exception localException) {
		}
	}
}
