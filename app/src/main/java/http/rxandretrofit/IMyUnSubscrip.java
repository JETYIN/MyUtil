package http.rxandretrofit;

/**
 * Created by Administrator on 2017/3/10.
 */

public interface IMyUnSubscrip {

    //定义接口

    /**
     * 采用mvp模式定义的presenter层基类接口
     **/
     interface BasePresenter<T> {
        void attachView(T view);

        void detachView();

    }

    /**
     * view层的基类接口
     **/
     interface BaseView {

        void showError();

        void complete();
    }
}
