package http.retrofit;

import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2018/6/25.
 */
//FIXME 可覆盖url,定制可覆盖url的retrofit与okhttp
public class ReCenter implements IRetrofit {

    private final int DEFAULT_TIME = 10000;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private API service;

    private String BASE_URL = "http://localhost:8080/student";//FIXME 设置网络请求头

    public static ReCenter getInstance() {
        return SingleTon.INSTANCE.getInstance();
    }

    @Override
    public void post(String url, Map<String, String> map, final IHttpResponse callBack) {

        Call<ResponseBody> call = service.post(url, map);// 此处覆盖默认设置的base_url
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                // TODO Auto-generated method stub
                try {
                    callBack.onFailed(throwable.toString());
                } catch (Exception e) {
                    callBack.onFailed(e != null ? e.toString() : "");
                }
            }

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // TODO Auto-generated method stub

                try {
                    String data = response.body().string();// 获取数据

                    if (TextUtils.isEmpty(data)) {
                        callBack.onFailed("net_work_error");
                        return;
                    }
                    callBack.onSucessed(data);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    callBack.onFailed(e != null ? e.toString() : "");
                }
            }

        });
    }

    @Override
    public void post(String url, RequestBody body, IHttpResponse callBack) {

    }

    private static enum SingleTon {
        INSTANCE;
        private ReCenter instance;

        private SingleTon() {

            instance = new ReCenter();
        }

        private ReCenter getInstance() {
            return instance;
        }
    }


    private ReCenter() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder().retryOnConnectionFailure(false).addInterceptor(interceptor)// 添加拦截器
                .connectTimeout(DEFAULT_TIME, TimeUnit.MILLISECONDS).readTimeout(DEFAULT_TIME, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_TIME, TimeUnit.MILLISECONDS).build();

        //
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)// 设置基准url
                .client(okHttpClient).build();

        service = retrofit.create(API.class);
    }


    /**
     * 以下实现接口
     **/

}
