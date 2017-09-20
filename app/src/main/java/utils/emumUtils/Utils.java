package utils.emumUtils;

/**
 * Created by Administrator on 2017/8/24.,获取泛型中的数据类型
 */

public class Utils {


    public interface ICreateImp<T> {

        public void createor(T message);//定义生成器，用于工厂模式中,或是使用在回调中，用于指定回调参数的参数类型在new的时候传入T的类型,可通过反射方法获知接口中的泛型类型，而后对其参数类型加以判断
    }

}
