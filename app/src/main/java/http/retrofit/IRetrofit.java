package http.retrofit;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/6/25.
 */

public interface IRetrofit {

    void post(String url, Map<String, String> map, IHttpResponse callBack);

    void post(String url, RequestBody body, IHttpResponse callBack);

}
