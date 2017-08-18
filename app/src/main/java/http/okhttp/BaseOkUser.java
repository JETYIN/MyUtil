package http.okhttp;

/**
 * Created by Administrator on 2017/8/17.
 */


import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 此类描述okHttp的基本使用，只是作为基本demo参照,okhttp类似于volley，在使用中应只保持一个对象
 **/
public class BaseOkUser {

    String url = "http://www.baidu.com";
    //https://api.douban.com/v2/movie/top250?start=0&count=10  ,此url拥有数据

    Handler handler = new Handler();


    private OkHttpClient okHttpClient;


    private OkHttpClient getHttp() {

        if (okHttpClient == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            //创建okHttp对象
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging) //设置拦截器
                    .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)//设置失败重连
                    .build();
        }
        return okHttpClient;

    }

    //get请求
    private void get() {
        //1.创建okhttpclient对象
        OkHttpClient client = getHttp();

        //2.创建请求对象Request
        Request request = new Request.Builder().url(url).build();//Request中可设置请求头等

        //3.创建一个call对象
        Call call = client.newCall(request);

        //4.将请求添加到调度中(消息队列)

        call.enqueue(new Callback() { //使用enqueue，回调运行于网络线程，在回调中需另行处理ui
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //response为请求url返回数据
                if (response.isSuccessful()) {

                    String message = response.message().toString();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //需在此处处理ui刷新等操作

                        }
                    });

                }
            }
        });

    }

    //post请求
    private void post(HashMap<String, String> jsonMap) {

        //1.创建http对象
        OkHttpClient okHttp = getHttp();
        //2.创建请求对象--post请求请求体不同

        RequestBody body = new FormBody.Builder().add("userName", "password").build();

        //3.创建请求
        Request request = new Request.Builder().url(url).post(body).build();

        //4.创建call对象
        Call call = okHttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
    /**提供上传文件等操作**/



}


