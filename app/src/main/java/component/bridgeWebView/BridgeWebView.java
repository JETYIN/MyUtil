package component.bridgeWebView;

/**
 * Created by Administrator on 2017/8/16.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.lang.reflect.Method;

/**
 * 封装js和android通信
 **/
public class BridgeWebView extends WebView {

    /***
     * js调用android方法的映射字符串
     **/
    private static final String JS_INTERFACE = "jsInterface";
    private String TAG = "BridgeWebView";

    public BridgeWebView(Context context) {
        super(context);
    }

    public BridgeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public BridgeWebView(Context context, AttributeSet attrs, int def) {
        super(context, attrs, def);
    }


    /**
     * 注册js和android通信桥梁对象
     *
     * @param obj 桥梁类对象,该对象提供方法让js调用,默认开启JavaScriptEnabled=true
     */
    public void addBridgeInterface(Object obj) {//此处传入的是一个集中方法使用类
        this.getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(new JavaJsBridgeObj(obj), JS_INTERFACE);
    }

    /**
     * 注册js和android通信桥梁对象
     *
     * @param obj 桥梁类对象,该对象提供方法让js调用
     * @param url 默认开启JavaScriptEnabled=true
     */
    public void addBridgeInterface(Object obj, String url) {
        this.getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(new JavaJsBridgeObj(obj), JS_INTERFACE);
        loadUrl(url);
    }


    /**
     * 回调js方法
     *
     * @param json 参数，json格式字符串
     */
    public void callbackJavaScript(String json) {

    }

    private void invokeJavaScript(String callback, String params) {

    }

    //内置js--android通信桥梁类

    public class JavaJsBridgeObj {

        //需要传递的参数
        Object contentObj;
        //通过反射方法传递数据

        public JavaJsBridgeObj(Object obj) {
            this.contentObj = obj;
        }

        /**
         * 内置桥梁方法
         *
         * @param method 方法名
         * @param json   js传递参数，json格式
         */
        @JavascriptInterface
        public void invokeBridgeMethod(String methodName, String[] json) {

            Log.e(TAG, "****invoke--invokeBridgeMethod");

            Class<?>[] params = new Class[]{String.class};
            Method targetMethod = null;
            try {
                targetMethod = this.contentObj.getClass().getDeclaredMethod(methodName, params);
                targetMethod.invoke(contentObj, new Object[]{json});//反射调用js传递过来的方法，传参
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
