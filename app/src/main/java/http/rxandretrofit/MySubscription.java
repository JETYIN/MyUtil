package http.rxandretrofit;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/3/10.
 */

/**
 * 此类将在mvp模式中的presenter层中被继承
 **/
public class MySubscription<T extends IMyUnSubscrip.BaseView> implements IMyUnSubscrip.BasePresenter<T> {

    protected T view;
    /**
     * 观察者订阅被观察者后会生成订单，在退出activity时应该对订单做相应处理，不然可能触发oom
     **/


    private CompositeSubscription compositeSubscription;

    /**
     * activtity中进行调用
     **/
    public void addSubscrip(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    /**
     * 解除订单
     **/
    public void quiteSubscri() {
        if (compositeSubscription != null) {

            compositeSubscription.unsubscribe();
        }
    }


    /**
     * ----通过接口，实现接口，再让baseactivity继承他，或是使用mvp模式，让presenter继承此类
     **/

    /**
     * 主要运用于采用mvp的模式中，，既是view层和presenter层的业务分离，对于Fragment当退出时，进行取消订单以及view
     **/
    @Override
    public void attachView(T view) {

        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        quiteSubscri();

    }


}
