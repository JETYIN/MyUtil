package http.retrofit;

/**
 * Created by Administrator on 2018/6/25.
 */

public interface IHttpResponse {//FIXME 返回未经处理的response string，app中一般不用

    void onSucessed(String response);

    void onFailed(String response);

}
