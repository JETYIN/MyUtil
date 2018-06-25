package http.retrofit;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2018/6/25.
 */

public interface API {
    @GET
    Call<ResponseBody> get(@Url String url);

    @GET
    Call<ResponseBody> get(@Url String url, @Body RequestBody requestBody);

    @POST
    Call<ResponseBody> post(@Url String url);

    @POST
    @FormUrlEncoded
    Call<ResponseBody> post(@Url String url, @FieldMap Map<String, String> maps);

    @POST
    Call<ResponseBody> post(@Url String url, @Body RequestBody requestBody);
}
