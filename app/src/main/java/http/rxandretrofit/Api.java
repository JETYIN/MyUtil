package http.rxandretrofit;

import java.util.List;

import http.rxandretrofit.etity.BookListTags;
import http.rxandretrofit.etity.BookSource;
import http.rxandretrofit.etity.Recommend;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by Administrator on 2017/3/10.
 */

public class Api {

    /**
     * 创建一个类用于处理网络请求回调
     **/
    public static Api instance;

    /**
     * APIService进行网络数据的请求，在此类进行回调成功数据
     **/
    private APIService service;

    /**
     * 构造方法创建retrofit
     **/
    private Api(OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit.Builder()
                //基准url
                .baseUrl("http://")
                //添加rxjava转换工厂
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                //绑定okhttp
                .client(okHttpClient)
                .build();

        /**将APIService实例化,service返回的是Oberservable包装的实体---被观察者**/


        service = retrofit.create(APIService.class);


    }

    public static Api getInstance(OkHttpClient okHttpClient) {
        if (instance == null) {
            synchronized (Api.class) {
                if (instance == null) {
                    instance = new Api(okHttpClient);
                }
            }
        }
        return instance;
    }

    /**将APIService中的请求成功回调**/

    /**
     * 将在BookApI中返回的Observable进行封装数据返回
     **/

    public Observable<Recommend> getRecommend(String gender) {
        return service.getRecomend(gender);
    }

    public Observable<List<BookSource>> getBookSource(String view, String book) {

        return service.getBookSource(view, book);
    }

    public Observable<BookListTags> getBookListTags() {
        return service.getBookListTags();

    }

}
