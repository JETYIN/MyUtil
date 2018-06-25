package http.retrofit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/25.
 */

public class RetrofitFactory {

    private static IRetrofitFactory FACTORY;

    static {
        FACTORY = new FactoryCreateInstance();
    }


    public static IRetrofit with() {//FIXME 外部调用创建对象接口
        return FACTORY.create();
    }

    interface IRetrofitFactory {
        IRetrofit create();
    }


    private static class FactoryCreateInstance implements IRetrofitFactory {

        @Override
        public IRetrofit create() {

            return ReCenter.getInstance();
        }
    }


    //FIXME 模拟进行网络请求
    private void test() {
        Map<String, String> postData = new HashMap<>();

        postData.put("username", "username");
        postData.put("password", "password");
        RetrofitFactory.with().post("/login", postData, new IHttpResponse() {
            @Override
            public void onSucessed(String response) {

            }

            @Override
            public void onFailed(String response) {

            }
        });
    }
}
