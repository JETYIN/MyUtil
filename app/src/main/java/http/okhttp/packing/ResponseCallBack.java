package http.okhttp.packing;

/**
 * Created by Administrator on 2017/8/18.
 */

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;

/**
 * 创建类似volley中的的抽象ResponseCallBack类
 **/

public abstract class ResponseCallBack<T> {

    private String TAG = "ResponseCallBack";
    public Type mType;//用于反射,判断网络请求数据是String 还是具体的实体类

    /**
     * 此处封装请求前、成功、失败、请求
     **/

    /**
     * 通过反射想要的返回类型
     *
     * @param subclass
     * @return
     */

    //留于外部类实现时的new方法
    public ResponseCallBack() {
        mType = getSuperclassTypeParameter(getClass());

    }

    //ResponseCallBack中设置的返回泛型数据
    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }


    //用于请求之前进行ui操作
    public void onRequestBefore(Request request) {
    }


    //进行相应的ui操作
    public void onRequestAfter() {
    }


    //请求失败
    public abstract void onResponseError(Request request, Exception e);

    //请求成功
    public abstract void onResponseSuc(T response);

}
