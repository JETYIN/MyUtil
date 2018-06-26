package http.dagger2;

import android.app.Application;

import work.dylan.com.rxdemo.dagger2.component.AppComponent;
import work.dylan.com.rxdemo.dagger2.component.DaggerAppComponent;
import work.dylan.com.rxdemo.dagger2.module.ApiModule;
import work.dylan.com.rxdemo.dagger2.module.AppModule;


/**
 * Created by Administrator on 2018/6/26.
 */

public class RxApplication extends Application {

    private AppComponent appComponent;//FIXME 提供接口获取

    private static RxApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initDagger2();
    }

    //FIXME 初始化dagger2组件
    private void initDagger2() {//FIXME 初始化dagger2 module部分,Api

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).apiModule(new ApiModule()).build();
    }

    public static RxApplication getInstance() {
        return instance;
    }

    public AppComponent getAppComponent() {//FIXME  提供给baseActivity进行封装
        return appComponent;
    }

}
