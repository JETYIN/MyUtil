package http.dagger2.mvp.book;

import work.dylan.com.rxdemo.dagger2.mvp.IBaseMvp;
import work.dylan.com.rxdemo.dagger2.test.Book;

/**
 * Created by Administrator on 2018/6/26.
 */

public interface IBookMvp {

    interface IView extends IBaseMvp.IBaseView {//FIXME 将请求网络返回数据用于刷新ui

        void showQueryBookById(Book data);//返回查询值

    }

    interface IPresenter<T> extends IBaseMvp.IBasePresenter<T> {//FIXME T为实现Iview接口的Activity实现,请求网络任务

        void getBookById(String bookId);//查询
    }
}
