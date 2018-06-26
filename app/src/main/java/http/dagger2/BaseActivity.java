package http.dagger2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import work.dylan.com.rxdemo.dagger2.component.AppComponent;

/**
 * Created by Administrator on 2018/6/26.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("dylan", "****base oncreate");
        init();
    }


    private void init() {
        setComponent(RxApplication.getInstance().getAppComponent());
    }

    protected abstract void setComponent(AppComponent appComponent);//FIXME 注解了Context、ApiCenter,要使用Apicengt，需在实现类Activity中注解inject，即可使用Apicenter

}
