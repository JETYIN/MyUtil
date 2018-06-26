package http.dagger2.test;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import work.dylan.com.rxdemo.dagger2.BaseActivity;
import work.dylan.com.rxdemo.dagger2.component.AppComponent;
import work.dylan.com.rxdemo.dagger2.component.DaggerProductComponent;

/**
 * Created by Administrator on 2018/6/26.
 */

public class ProductActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void setComponent(AppComponent appComponent) {//FIXME 此处将apicent注解进activity
        DaggerProductComponent.builder().appComponent(appComponent).build().inject(this);
    }
}
