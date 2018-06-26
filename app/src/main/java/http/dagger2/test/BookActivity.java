package http.dagger2.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import javax.inject.Inject;

import work.dylan.com.rxdemo.R;
import work.dylan.com.rxdemo.dagger2.BaseActivity;
import work.dylan.com.rxdemo.dagger2.component.AppComponent;
import work.dylan.com.rxdemo.dagger2.component.DaggerBookComponent;
import work.dylan.com.rxdemo.dagger2.mvp.book.BookPresenter;
import work.dylan.com.rxdemo.dagger2.mvp.book.IBookMvp;

/**
 * Created by Administrator on 2018/6/26.
 */

public class BookActivity extends BaseActivity implements IBookMvp.IView {

    @Inject
    BookPresenter bookPresenter;//FIXME 将mvp表现层中的数据注入到具体的BookActivity

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        //FIXME 绑定界面与查询数据
        Log.e("dylan", "*****oncreate");
        bookPresenter.attach(this);
        bookPresenter.getBookById("dylan");//FIXME 请求http任务
    }


    @Override
    public void setComponent(AppComponent appComponent) {//FIXME 此步之后可以使用apicenter
        DaggerBookComponent.builder().appComponent(appComponent).build().inject(this);//FIXME 此处inject使用的BookComponent中声明的接口
    }


    @Override
    public void showQueryBookById(Book data) {

        Log.e("dylan", "success");
    }

    @Override
    public void showError() {
        Toast.makeText(BookActivity.this, "connection error", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void complete() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != bookPresenter) {

            bookPresenter.detach();
        }
    }
}
