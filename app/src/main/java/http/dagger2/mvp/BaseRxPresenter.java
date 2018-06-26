package http.dagger2.mvp;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/6/26.
 */

public class BaseRxPresenter<T extends IBaseMvp.IBaseView> implements IBaseMvp.IBasePresenter<T> {

    //FIXME RxPresenter作为其他具体实现presenter的基类，用于管理Rx的生命周期，以免Fragment和创建的subcription未回收，触发oom
    protected T mView;

    protected CompositeSubscription mCompositeSubscription;


    protected void addSubscriber(Subscription subscription) {//FIXME 用于Rx创建网络任务时调用，以管理声明周期
        if (null == mCompositeSubscription) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    protected void unSubscriber() {//FIXME detach时调用，释放subscription
        if (null == mCompositeSubscription) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void attach(T view) {//FIXME 实现view接口的Activity与presenter接口实现的绑定
        this.mView = view;
    }

    @Override
    public void detach() {
        this.mView = null;
        unSubscriber();
    }
}
