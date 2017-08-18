package http.okhttp.packing;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2017/8/18.
 */

public class OkHttpManager {

    private static OkHttpManager instance;
    private String TAG = getClass().getSimpleName();
    private OkHttpClient okHttpClient;
    //用于分发请求结果
    private Handler mHandler;
    private Gson mGson;

    //私有构造中实现okhttp的配置---只是针对http连接，未设置https方式
    private OkHttpManager() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //创建okHttp对象
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging) //设置拦截器
                .connectTimeout(15, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)//设置失败重连
                .build();

        //创建数据处理
        mGson = new Gson();
    }


    /***
     * dcl模式下的单例模式
     */
    private static OkHttpManager getInstance() {
        if (null == instance) {
            synchronized (OkHttpManager.class) {
                if (null == instance) {
                    instance = new OkHttpManager();
                }
            }
        }
        return instance;

    }

    /**
     * @params url
     * prams --请求参数
     * callback--结果回调
     * tag--事件处理
     **/

    public void post(String url, Map<String, String> params, ResponseCallBack responseCallBack, Object flag) {

        Request request = buildeRequest(url, params, flag);
        //调用请求
        deliverResponse(request, responseCallBack);

    }

    /**
     * @params
     **/

    public void get() {
    }

    //网络请求回调

    private void deliverResponse(final Request request, final ResponseCallBack responseCallBack) {

        //ui线程，请求之前
        responseCallBack.onRequestBefore(request);

        //当前网络请求异步执行--执行于子线程
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败回调
                responseCallBack.onResponseError(request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseMessage = response.message();
                final String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    //需求是String的返回数据类型
                    Log.e(TAG, "****response isSuccessful");
                    if (responseCallBack.mType == String.class) {
                        sendSucess(responseBody, responseCallBack);

                    } else {//返回类型是实体类需要Gson
                        Object obj = mGson.fromJson(responseBody, responseCallBack.mType);
                        sendSucess(obj, responseCallBack);

                    }

                } else {
                    Log.e(TAG, "----response fail");
                    Exception exception = new Exception(response.code() + ":" + responseMessage);
                    sendFail(response.request(), exception, responseCallBack);
                }
            }
        });

    }

    //成功后将数据发回主线程--可在onResponseSuc方法中进行ui更新
    private void sendSucess(final Object object, final ResponseCallBack responseCallBack) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                responseCallBack.onResponseSuc(object);
                responseCallBack.onRequestAfter();

            }
        });

    }

    private void sendFail(final Request request, final Exception e, final ResponseCallBack responseCallBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                responseCallBack.onResponseError(request, e);
                responseCallBack.onRequestAfter();
            }
        });
    }


    //绑定请求---请求参数还未封装
    private Request buildeRequest(String url, Map<String, String> params, Object flag) {


        return null;
    }


}
