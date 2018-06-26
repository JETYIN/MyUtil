package http.dagger2;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import work.dylan.com.rxdemo.dagger2.test.Book;

/**
 * Created by Administrator on 2018/6/26.
 */

public class ApiCenter {
    private String API_BASE_URL = "http://localhost:8080/";

    private Api apiService;//FIXME retofit接口定义服务

    private static ApiCenter instance;
    private Retrofit retrofit;

    public ApiCenter(OkHttpClient okHttpClient) {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(okHttpClient)
                .build();
        apiService = retrofit.create(Api.class);
    }


    public static ApiCenter getInstance(OkHttpClient okHttpClient) {
        if (instance == null) {
            synchronized (ApiCenter.class) {
                if (null == instance) {
                    instance = new ApiCenter(okHttpClient);
                }
            }
        }
        return instance;
    }

    /**
     * --------以下获取对应接口数据------
     **/

    public Observable<Book> getBookId(String bookId) {//FIXME 根据id查询书籍
        return apiService.getBookId(bookId);


    }


}
