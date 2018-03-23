package utils.ok_re;

import java.util.Map;

import com.funcell.platform.android.http.okhttp3.RequestBody;

public interface IFunRequest {// 定义请求方法

	void post(String url, Map<String, String> map, IFuncellHttpCallBack callBack);

	void post(String url, RequestBody body, IFuncellHttpCallBack callBack);
}
