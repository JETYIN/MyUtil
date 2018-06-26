package http.dagger2.component;

import android.content.Context;

import dagger.Component;
import work.dylan.com.rxdemo.dagger2.ApiCenter;
import work.dylan.com.rxdemo.dagger2.module.ApiModule;
import work.dylan.com.rxdemo.dagger2.module.AppModule;


/**
 * Created by Administrator on 2018/6/26.
 */

@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    Context getContext();//FIXME 提供application上下文

    ApiCenter getReaderApi();//FIXME 提供rx api接口中心

}
