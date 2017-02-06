package http.volley;

import com.android.volley.Response;
import com.android.volley.VolleyError;


/**
 * 此处重写Response的ErrorListener接口，和Listener接口下的方法
 **/
public abstract class ResponseCallback<E> implements Response.ErrorListener, Response.Listener<E> {
    private static final String TAG = ResponseCallback.class.getSimpleName();

    public ResponseCallback() {
    }

    /**
     * 请求错误处理
     **/
    @Override
    public void onErrorResponse(VolleyError volleyError) {


    }

    /**
     * Response  Listener接口下的方法
     **/
    @Override
    public void onResponse(E result) {
        if (result != null) {
            /**请求成功**/
            onRequestSuccess(result);
        } else {
            /**请求错误处理**/
            //onReuquestFailed(new ErrorBase("-1", "error");
        }
       /* if (result == null) {
            onReuquestFailed(new ErrorBase("-1", XinlaiApplication.getAppContext().getResources().getString(R.string.network_none_return)));

            return;
        }
        if (result instanceof ResponseBase) {
            ResponseBase responseBase = (ResponseBase) result;
            if ("SUCCESS".equals(responseBase.status)) {
                onRequestSuccess(result);
            } else {
                *//**错误状态码和错误信息**//*
                onReuquestFailed(new ErrorBase(responseBase.status, responseBase.msg));
                if (Constant.IS_DEBUG_MODE) {
                    Logger.d(TAG + " ERROR", responseBase.msg == null ? "NULL" :
                            responseBase.msg);
                }
            }
        } else {
            onRequestSuccess(result);
        }*/
    }

    public abstract void onRequestSuccess(E result);

    public abstract void onReuquestFailed(E error);
}
