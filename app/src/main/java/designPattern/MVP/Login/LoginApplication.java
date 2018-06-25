package designPattern.MVP.Login;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Administrator on 2018/5/23.
 */


public class LoginApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            Log.e("dylan", "-------leakCanary return");
            return;
        }
        LeakCanary.install(this);
    }
}
