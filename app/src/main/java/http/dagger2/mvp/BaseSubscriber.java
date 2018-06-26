package http.dagger2.mvp;

import rx.Subscriber;

/**
 * Created by Administrator on 2018/6/26.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {
    //FIXME 此处可封装统一处理需求
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        _onError(e.getMessage());
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }


    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);
}
