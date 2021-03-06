package component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.work.xinlai.R;
import com.work.xinlai.common.Logger;
import com.work.xinlai.http.HttpUtils;

/**
 * Created by Administrator on 2016/11/23.
 */
public abstract class BaseActivity extends Activity {

    /**
     * 对网络请求的处理
     **/
    public String NETTAG;
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;


    private ImageButton leftIB;

    private ImageButton rightIB;

    private TextView titleTV;

    /**
     * 动态设置布局资源文件
     **/
    protected abstract int getLayoutResourceId();

    /**
     * base布局s
     **/
    protected int getBaseLayoutResID() {
        return R.layout.activity_base;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getBaseLayoutResID());
        init();
        this.mContext = this;
        NETTAG = mContext.getClass().getSimpleName();
        Logger.e("BaseActivity", NETTAG);
        Logger.e("CPU", String.valueOf(Runtime.getRuntime().availableProcessors()));
        mLayoutInflater = getLayoutInflater();
        /**加载baseactivity—title-viewstub**/

        int layoutResID = getLayoutResourceId();
        if (layoutResID != 0) {
            ViewStub vs = (ViewStub) findViewById(R.id.vs_content);
            vs.setLayoutResource(getLayoutResourceId());
            vs.inflate();
        }
        /**图片按钮后退**/
        leftIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void init() {
        leftIB = (ImageButton) findViewById(R.id.btn_left);
        rightIB = (ImageButton) findViewById(R.id.btn_right);
        titleTV = (TextView) findViewById(R.id.tv_title);

    }

    protected void hideLiftImage() {
        leftIB.setVisibility(View.GONE);
    }

    /**
     * s设置标题
     **/
    public void showTitle(CharSequence txt) {
        titleTV.setText(txt);
    }


    /**
     * 当activity stop时取消网络连接，Tag为空时取消所有网络请求
     **/
    @Override
    protected void onStop() {
        super.onStop();
        HttpUtils.cancelAll(NETTAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 页面被结束时设置动画overridePendingTransition (int enterAnim, int exitAnim)
     * enterAnim： 进入的动画
     * exitAnim：退出动画
     **/

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    protected void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    public void showToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    //FIXME 检测版本使用状态栏全透明,继承自baseActivity需在setContentView之前调用
    protected void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
//            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS); // 新增滑动返回，舍弃过渡动效

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
