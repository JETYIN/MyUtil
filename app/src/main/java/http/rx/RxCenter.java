package http.rx;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/6/25.
 */
public class RxCenter {
    private String TAG = "RxCenter";
    private final int CONNECT_TIME = 10 * 1000;
    private final int TIME_OUT = 20 * 1000;
    private Retrofit retrofit;
    private String BASE_URL = "http://localhost:8080/student";

    private RxCenter() {//FIXME 创建okhttp与retrofit
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(CONNECT_TIME, TimeUnit.MILLISECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) // 失败重发
                .addInterceptor(interceptor);
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(builder.build())//添加okhttp
                .build();

        //service = retrofit.create(BookApiService.class);FIXME 为了区别与创建不同的api入口

    }

    //FIXME 此处预留retrofit创建接口
    public <T> T create(Class<T> tClass) {
        return retrofit.create(tClass);
    }

    public static RxCenter getInstance() {//FIXME 外部调用入口
        return SingleTon.SINGLETON.getInstance();
    }

    private static enum SingleTon {

        SINGLETON;
        private RxCenter instance;

        private SingleTon() {
            instance = new RxCenter();
        }

        public RxCenter getInstance() {
            return instance;
        }
    }


}
