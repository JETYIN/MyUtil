package utils.ok_re;

import java.util.Map;

import com.funcell.platform.android.http.okhttp3.RequestBody;
import com.funcell.platform.android.http.okhttp3.ResponseBody;
import com.funcell.platform.android.http.retrofit2.Call;
import com.funcell.platform.android.http.retrofit2.http.Body;
import com.funcell.platform.android.http.retrofit2.http.FieldMap;
import com.funcell.platform.android.http.retrofit2.http.FormUrlEncoded;
import com.funcell.platform.android.http.retrofit2.http.GET;
import com.funcell.platform.android.http.retrofit2.http.POST;
import com.funcell.platform.android.http.retrofit2.http.Url;

/***
 * 定制整个lib包中的api,注册、登录、支付,此处不注册url地址因url地址因会覆盖base_url，代码中会传入绝对地址
 * 
 * @author Administrator
 *
 */
public interface RetrofitSevice {

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
