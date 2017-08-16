package utils.singleton;

/**
 * Created by Administrator on 2017/8/8.
 */

public abstract class SingletonUtils<T> {

    private T instance;

    private SingletonUtils() {
    }

    /**留于需子类实现的构造方法**/
    public abstract T creatSingleTon();

    public T getInstance() {
        if (instance == null) {
            synchronized (SingletonUtils.class) {
                if (instance == null) {
                    instance = creatSingleTon();
                }
            }
        }
        return instance;
    }

}
