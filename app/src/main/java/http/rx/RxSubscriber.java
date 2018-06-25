package http.rx;

import rx.Subscriber;

/**
 * Created by Administrator on 2018/6/25.
 */

public abstract class RxSubscriber<T> extends Subscriber<T> {//此类可封装ui等待操作


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {

    }


    abstract void _onNext(T t);

    abstract void _onError(String message);
}
