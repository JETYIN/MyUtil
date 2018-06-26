package http.dagger2.mvp.book;


import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import work.dylan.com.rxdemo.dagger2.ApiCenter;
import work.dylan.com.rxdemo.dagger2.mvp.BaseRxPresenter;
import work.dylan.com.rxdemo.dagger2.mvp.BaseSubscriber;
import work.dylan.com.rxdemo.dagger2.test.Book;

/**
 * Created by Administrator on 2018/6/26.
 */

public class BookPresenter extends BaseRxPresenter<IBookMvp.IView> implements IBookMvp.IPresenter<IBookMvp.IView> {
    //FIXME 此presenter获取rx网络任务，进行网络请求.返回数据给view层

    private ApiCenter apiCenter;
    private Context context;

    @Inject
    public BookPresenter(ApiCenter apiCenter, Context context) {//FIXME 留于Activity中实现注入BookPresenter
        this.apiCenter = apiCenter;
        this.context = context;
    }

    @Override
    public void getBookById(String bookId) {
        //fixme 此处可以继续封装Observer接口
        Subscription subscription = apiCenter.getBookId(bookId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<Book>() {
            @Override
            protected void _onNext(Book book) {
                mView.showQueryBookById(book);//FIXME 请求结果返回实现biew层的activity
            }

            @Override
            protected void _onError(String message) {
                Log.e("dylan", "-----------:" + message);
                mView.showError();
            }
        });
        addSubscriber(subscription);//FIXME 每次个请求都会生成subscription对象，需要对此进行周期管理，以免在Fragment奔溃或对象不能被释放造成oom
    }
}
