package utils.autoLogin;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class FunQuickLoginDialog extends Dialog implements View.OnClickListener {

	private Context mContext;

	private TextView accountTV, switchTV;
	private FrameLayout contentFL;
	private ImageView lodingIM;

	private IQuickLoginClick listDialogClick;

	//dialog动画，从顶部显示
	public FunQuickLoginDialog(Context context, String account) {
		this(context, RUtils.style(context, "Translucent_NoTitle"), account);
	}

	public FunQuickLoginDialog(Context context, int theme, String account) {
		super(context, theme);
		mContext = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(RUtils.layout(context, "fun_quick_login"));
		//设置全屏--不设置此语句顶部会被状态栏遮挡
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
		// 位置
		getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		// 出现、消失动画
		getWindow().setWindowAnimations(RUtils.style(mContext, "fun_quicklogin_dialog"));

		setCancelable(false);
		setCanceledOnTouchOutside(false);
		initView(account);
	}

	private void initView(String account) {

		accountTV = (TextView) findViewById(RUtils.id(mContext, "account_tv"));
		switchTV = (TextView) findViewById(RUtils.id(mContext, "switch_tv"));
		contentFL = (FrameLayout) findViewById(RUtils.id(mContext, "content_fl"));
		lodingIM = (ImageView) findViewById(RUtils.id(mContext, "loding_image"));
		lodingAnim(lodingIM);
		accountTV.setText(account);
		switchTV.setOnClickListener(this);
	}

	private void lodingAnim(ImageView im) {

		Animation animation = AnimationUtils.loadAnimation(mContext, RUtils.anim(mContext, "fun_quick_loding"));

		im.startAnimation(animation);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == RUtils.id(mContext, "switch_tv")) {
			FunCellPlatformSdkApi.getInstance().setIsSwitchUser(true);
			listDialogClick.onSwitchClick();
		}
	}

	public void rigisterListDialogClick(IQuickLoginClick listener) {
		listDialogClick = listener;
	}

	public void unrigisterListDialogClick() {
		listDialogClick = null;
	}

	/**
	 * 回调接口
	 **/
	public interface IQuickLoginClick {
		void onSwitchClick();
	}
}
