package utils.ok_re;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.funcell.platform.android.http.okhttp3.OkHttpClient;
import com.funcell.platform.android.http.okhttp3.RequestBody;
import com.funcell.platform.android.http.okhttp3.ResponseBody;
import com.funcell.platform.android.http.okhttp3.logging.HttpLoggingInterceptor;
import com.funcell.platform.android.http.retrofit2.Call;
import com.funcell.platform.android.http.retrofit2.Callback;
import com.funcell.platform.android.http.retrofit2.Response;
import com.funcell.platform.android.http.retrofit2.Retrofit;

import android.text.TextUtils;
import android.util.Log;

public class RetrofitManager implements IFunRequest {

	private static RetrofitManager instance;
	private final int DEFAULT_TIME = 10;
	private OkHttpClient okHttpClient;
	private Retrofit retrofit;
	private String TAG = "RetrofitManager";
	private String BASE_URL = "http://sdk.funcell123.com/";// base_url不加会报错,设置后，后面设置url会覆盖

	private RetrofitSevice service;// 注解的接口api

	public static RetrofitManager getInstance() {
		if (null == instance) {
			synchronized (RetrofitManager.class) {
				if (null == instance) {
					instance = new RetrofitManager();
				}

			}
		}
		return instance;
	}

	private RetrofitManager() {// 创建单例的okhttp和retrofit
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		okHttpClient = new OkHttpClient.Builder().retryOnConnectionFailure(false).addInterceptor(interceptor)// 添加拦截器
				.connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS).readTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
				.writeTimeout(DEFAULT_TIME, TimeUnit.SECONDS).build();

		//
		retrofit = new Retrofit.Builder().baseUrl(BASE_URL)// 设置基准url
				.client(okHttpClient).build();

		service = retrofit.create(RetrofitSevice.class);
	}

	@Override
	public void post(String url, Map<String, String> map, final IFuncellHttpCallBack callBack) {
		// TODO Auto-generated method stub

		Log.e(TAG, "****invoke_post_map,url:" + url + ",map:" + map.toString());

		Call<ResponseBody> call = service.post(url, map);// 此处覆盖默认设置的base_url
		call.enqueue(new Callback<ResponseBody>() {

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable throwable) {
				// TODO Auto-generated method stub
				try {
					Log.e(TAG, "----failed,response:" + throwable.getMessage().toString());
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
						Log.e(TAG, "---------empty");
						callBack.onFailed("net_work_error");
						return;
					}
					callBack.onSucessed(data);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e(TAG, "----successed but occupry exception");
					callBack.onFailed(e != null ? e.toString() : "");
				}
			}

		});

	}

	@Override
	public void post(String url, RequestBody body, IFuncellHttpCallBack callBack) {
		// TODO Auto-generated method stub

	}

}
