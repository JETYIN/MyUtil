package http.rxandretrofit;

import java.util.concurrent.TimeUnit;

import http.rxandretrofit.etity.Constant;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2017/3/10.
 */

public class MyOkHttp {

    /**
     * 实例化OKHttpclient
     **/

    private OkHttpClient providerOkHttpClint() {

//创建OkHttp的日志拦截器

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(Constant.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(Constant.READ_TIMEOUT, TimeUnit.MILLISECONDS)
                /**失败重发---false--true参数**/
                .retryOnConnectionFailure(true)
                .addInterceptor(logging);

        /**client.build()的源码：----->
         *
         * public OkHttpClient build() {
           return new OkHttpClient(this);
         }**/
        return client.build();


    }

    /**
     * 返回提供APi,可以直接api.？？获取服务器返回的Observable
     **/
    public Api providerApi(OkHttpClient okHttpClient) {

        /**相当于激活了ApiService**/
        return Api.getInstance(okHttpClient);
    }


}
